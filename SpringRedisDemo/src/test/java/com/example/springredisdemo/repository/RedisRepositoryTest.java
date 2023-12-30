package com.example.springredisdemo.repository;

import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandType;
import io.lettuce.core.pubsub.RedisPubSubListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Range;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.domain.geo.GeoLocation;

import java.time.Duration;
import java.util.*;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisRepositoryTest {

    @Autowired StringRedisTemplate stringRedisTemplate;

    @BeforeEach
    void beforeEach() {
        stringRedisTemplate.getConnectionFactory().getConnection()
                .serverCommands().flushAll();
    }

    @Test
    @DisplayName("특수 명령어 Expiration (SET NX/XX)")
    public void specialCommandTest2() throws InterruptedException {
        Boolean mustBeTrue = stringRedisTemplate.opsForValue()
                .setIfAbsent("special:command:string", "value");
        Boolean mustBeFalse = stringRedisTemplate.opsForValue()
                .setIfAbsent("special:command:string", "value", Duration.ofSeconds(10));
        Boolean xxTest = stringRedisTemplate.opsForValue()
                .setIfPresent("not_valid_key", "not_valid_value");

        assertThat(mustBeTrue).isTrue();
        assertThat(mustBeFalse).isFalse();
        assertThat(xxTest).isFalse();

        Boolean mustBeTrue2 = stringRedisTemplate.opsForHash()
                .putIfAbsent("special:command:hash", "test_key", "test_value");
        Boolean mustBeFalse2 = stringRedisTemplate.opsForHash()
                .putIfAbsent("special:command:hash", "test_key", "test_value");
        assertThat(mustBeTrue2).isTrue();
        assertThat(mustBeFalse2).isFalse();
    }

    @Test
    @DisplayName("특수 명령어 Expiration (TTL)")
    public void specialCommandTest1() throws InterruptedException {
        stringRedisTemplate.opsForValue()
                .set("special:command:string", "value", Duration.ofSeconds(10));

        String beforeTtl = stringRedisTemplate.opsForValue().get("special:command:string");
        assertThat(beforeTtl).as("TTL 이 지나기전엔 값이 조회된다").isEqualTo("value");

        Thread.sleep(15000);

        String afterTtl = stringRedisTemplate.opsForValue().get("special:command:string");
        assertThat(afterTtl).as("TTL 이 지나면 값이 조회되지 않는다(nil)").isNull();
    }

    @Test
    @DisplayName("redisTemplate.opsForGeo() 로 Geospatial 자료구조를 사용할 수 있다.")
    public void geospatialTest() {
        GeoOperations<String, String> geoOperations = stringRedisTemplate.opsForGeo();
        geoOperations.add("seoul:station", new Point(126.83632075, 37.54875356), "ujangsan");
        geoOperations.add("seoul:station", new Point(126.83762897, 37.55863930), "balsan");
        geoOperations.add("seoul:station", new Point(126.84044370, 37.54165483), "hwagok");
        geoOperations.add("seoul:station", new Point(126.84664063, 37.53178245), "kachisan");
        geoOperations.add("seoul:station", new Point(126.82492659, 37.56026547), "magok");

        Distance distance = geoOperations.distance(
                "seoul:station", "ujangsan", "balsan", RedisGeoCommands.DistanceUnit.KILOMETERS
        );
        System.out.println("지점 사이의 거리 = " + distance.getValue());

        GeoResults<RedisGeoCommands.GeoLocation<String>> resultLocations = geoOperations.radius(
                "seoul:station",
                new Circle(
                        new Point(126.84044370, 37.54165483),
                        new Distance(1, RedisGeoCommands.DistanceUnit.METERS)
                )
        );
        if (resultLocations != null) {
            resultLocations.iterator()
                    .forEachRemaining(geoLocationGeoResult ->
                            System.out.println(geoLocationGeoResult.getContent().getName()));
        }

        GeoResults<RedisGeoCommands.GeoLocation<String>> simpleRaidus = geoOperations.radius(
                "seoul:station",
                "ujangsan",
                new Distance(1.5, RedisGeoCommands.DistanceUnit.KILOMETERS)
        );
        if (simpleRaidus != null) {
            simpleRaidus.iterator()
                    .forEachRemaining(geoLocationGeoResult ->
                            System.out.println(geoLocationGeoResult.getContent().getName()));
        }
    }

    @Test
    @DisplayName("redisTemplate.opsForHash() 로 Hash 자료구조를 사용할 수 있다.")
    public void hashTest() {
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        String hashKey = "redis:hash:test";
        hashOperations.put(hashKey, "first_hash", "hash_value_1");                    // HSET
        hashOperations.put(hashKey, "second_hash", "hash_value_2");
        Map<String, String> hashEntries = Map.of("third_hash", "hash_value_3", "fourth_hash", "hash_value_4");
        hashOperations.putAll(hashKey, hashEntries);

        Object hGet = hashOperations.get(hashKey, "third_hash");                            // HGET
        List<Object> hmGet = hashOperations.multiGet(hashKey, List.of("first_hash", "second_hash"));// HMGET
        Map<Object, Object> hGetAll = hashOperations.entries(hashKey);                              // HGETALL

        assertAll(
                () -> assertThat(hGet).isNotNull(),
                () -> assertThat(hGet).as("String 으로 형변환해서 사용해야 한다.").isExactlyInstanceOf(String.class),
                () -> assertThat(hGet).isEqualTo("hash_value_3")
        );
        assertThat(hmGet).allMatch(value -> value.getClass().isAssignableFrom(String.class));
        assertThat(hGetAll).hasSize(4);
    }

    @Test
    @DisplayName("redisTemplate.opsForZSet() 로 Sorted Sets 자료구조를 사용할 수 있다.")
    public void sortedSets() {
        ZSetOperations<String, String> sortedSetOperations = stringRedisTemplate.opsForZSet();

        String sortedSetKey = "redis:zset:test";
        Set<TypedTuple<String>> typeTupleValues = new HashSet<>() {
            {
                add(TypedTuple.of("zset_value_3", 1.0));
                add(TypedTuple.of("zset_value_2", 3.0));
                add(TypedTuple.of("zset_value_1", 6.0));
                add(TypedTuple.of("zset_value_2", 2.0));
                add(TypedTuple.of("zset_value_4", 5.0));
            }
        };
        sortedSetOperations.add(sortedSetKey, typeTupleValues);
        sortedSetOperations.add(sortedSetKey, "zset_value_5", 5.0);

        Long cardinality = sortedSetOperations.zCard(sortedSetKey);
        Set<String> zRange = sortedSetOperations.range(sortedSetKey, 0, -1);
        Set<String> zRangeReverse = sortedSetOperations.reverseRange(sortedSetKey, 0, -1);
        Set<TypedTuple<String>> rangeWithScores = sortedSetOperations.rangeWithScores(sortedSetKey, 0, -1);
        Set<TypedTuple<String>> reverseRangeWithScores = sortedSetOperations.reverseRangeWithScores(sortedSetKey, 0, -1);
        Long rank = sortedSetOperations.rank(sortedSetKey, "zset_value_2");

        assertThat(cardinality).as("카디널리티. 즉, 유일한 값의 개수 출력").isEqualTo(5);

        assertThat(zRange).as("Sorted Set 은 Score 를 기준으로 정렬한다. Score 가 같으면 사전편찬 순으로 순위가 정해진다.")
                .containsExactly("zset_value_3", "zset_value_2", "zset_value_4", "zset_value_5", "zset_value_1");

        assertThat(zRangeReverse).as("reverseRange() 는 range() 의 반대이다.")
                .containsExactly("zset_value_1", "zset_value_5", "zset_value_4", "zset_value_2", "zset_value_3");

        assertThat(rank).as("SortedSet 에서 랭크를 반환한다. 이는 Index 값과 같다.").isEqualTo(1);

    }

    @Test
    @DisplayName("redisTemplate.opsForSet() 로 Set 자료구조를 사용할 수 있다.")
    public void listsTest() {
        SetOperations<String, String> stringStringSetOperations = stringRedisTemplate.opsForSet();
        String setKey = "redis:set1:test";
        stringStringSetOperations.add(setKey, "first_value");
        stringStringSetOperations.add(setKey, "second_value", "third_value");
        stringStringSetOperations.add(setKey, "third_value", "fourth_value");
        String otherSetKey = "redis:set2:test";
        stringStringSetOperations.add(otherSetKey, "first_value", "third_value", "fifth_value");

        Set<String> sMembers = stringStringSetOperations.members(setKey);                   // SMEMBER
        Set<String> sInter = stringStringSetOperations.intersect(setKey, otherSetKey);      // SINTER
        Set<String> sDiff = stringStringSetOperations.difference(setKey, otherSetKey);      // SDIFF
        Set<String> sUnion = stringStringSetOperations.union(setKey, otherSetKey);          // SUNION
        Boolean sIsMember = stringStringSetOperations.isMember(setKey, "first_value");   // SISMEMBER

        assertThat(sMembers).as("members() 는 Redis 의 SMEMBERS 와 같다")
                .containsExactly("first_value", "second_value", "third_value", "fourth_value");
        assertThat(sInter).as("intersect() 는 Redis 의 SINTER 와 같다 (교집합)")
                .containsExactly("first_value", "third_value");
        assertThat(sDiff).as("difference() 는 Redis 의 SDIFF 와 같다 (차집합)")
                .containsExactly("second_value", "fourth_value");
        assertThat(sUnion).as("union() 는 Redis 의 SUNION 와 같다 (합집합)")
                .containsExactlyInAnyOrder("first_value", "second_value", "third_value", "fourth_value", "fifth_value");
        assertThat(sIsMember).as("isMember() 는 Redis 의 SISMEMBER 와 같다 (Set 의 멤버 여부)")
                .isTrue();
    }
    
    @Test
    @DisplayName("redisTemplate.opsForValue() 로 String 자료구조를 사용할 수 있다")
    public void setKeyTest() {
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.set("test:key", "test_value");
        String testKeyValue = stringRedisTemplate.opsForValue().get("test:key");

        assertThat(testKeyValue).isEqualTo("test_value");
    }
}
