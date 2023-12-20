해당 포스팅에서는 `DelegatingFilterProxy` 와 `FilterChainProxy` 에 대한 정의 역할 기능을 설명하며, 후반부에서는 직접 디버깅을 통해 `DelegatingFilterProxy`, `FilterChainProxy` 의 동작 원리를 분석해본다. 틀린 내용이 있으면 지적 부탁드립니다.

## 🔥 DelegatingFilterProxy

`DelegatingFilterProxy` 는 Servlet 이 지원하는 기능인 `Filter` 중 하나이다. 그 말은 즉, DelegatingFilterProxy 는 Servlet 에 도달하기 전에 호출된다는 것을 의미한다.

> DelegatingFilterProxy 는 스프링 시큐리티가 구현한 것이 아니며 Servlet FIlter 들 중 하나이다.

![](https://raw.githubusercontent.com/Revi1337/BlogImageFactory/main/spring/SpringSecurity/DelegatingFilterProxy_FilterChainProxy/Pasted%20image%2020231218233701.png)

<br>

기본적으로 DelegatingFilterProxy 는 Spring Container 로부터 `springSecurityFilterChain`  이라는 이름을 가지고있는  `Bean` 을 찾아온 다음,  해당 Bean 에게 사용자의 `Request` 를 처리해달라고 위임(`Delegate`)하는 역할을 한다.

> springSecurityFilterChain 이라는 Bean 이 FilterChainProxy 이다. 이것을 잊으면 안된다.

![](https://raw.githubusercontent.com/Revi1337/BlogImageFactory/main/spring/SpringSecurity/DelegatingFilterProxy_FilterChainProxy/Pasted%20image%2020231220021344.png)

> 정리하면 DelegatingFilterProxy 는 사용자로부터 들어온 Request 를 springSecurityFilterChain 이름의 FilterChainProxy 타입의 Bean 에 요청을 위임하는 역할만 수행하며, 실제적인 보안 처리를 수행하지 않는다.

## 🔥 FilterChainProxy

`FilterChainProxy` 는 DelegatingFilterProxy 로 부터 찾아진 springSecurityFilterChain 이름의 Bean 이다. DelegatingFilterProxy 로부터 사용자의 `Request` 를 처리해달라고 위임을 받았기 때문에 `실제로 보안 처리를 수행`한다. 이러한 보안 처리를 수행하기 위해 FilterChainProxy 는 여러개의 Filter 가 중첩된 Chain 형태를 이루며 보안 처리를 수행하게 된다.

<br>

FilterChainProxy 내부에서 일어나는 실제적인 보안처리의 핵심기능들을 요약해보면 아래의 다이어그램처럼 나타낼 수 있다.

4. FilterChainProxy 는 `getFilters()`  를 통해 Spring Security 초기화 당시 생성된 `Filter`   들을 조회하여 찾아온다.
5. 이렇게 찾아진 Filter 들을 순서대로 실행시키기 위한 `VirtualFilterChain` 이라는 가상필터체인이 FilterChainProxy 안에서 만들어진다.
6. `VirtualFilterChain` 의 doFilter() 를 호출하여 Filter 들을 순서대로 실행하여 보안처리를 시작한다.

<br>

해당 Filter 들이 모두 끝나야 비로소 Spring MVC 의 `DispatcherServlet` 에 도달하게 된다.

![](https://raw.githubusercontent.com/Revi1337/BlogImageFactory/main/spring/SpringSecurity/DelegatingFilterProxy_FilterChainProxy/Pasted%20image%2020231220013517.png)


이 때 중요한 것이 있다. `VirtualFilterChain` 이 조회된 Filter 들을 반복문을 돌면서 실행하는 것이 아니다. VirtualFilterChain 로부터 첫번째 필터가 호출되면 첫번째 필터는 할일을 수행하고 할일이 끝나면 다시 VirtualFilterChain 를 호출한다. 이제 VirtualFilterChain 은 두번째 필터를 호출하고 첫번쨰 과정을 반복한다. 다이어그램으로 그려보면 아래와 같다. 이 모든 것은 `doFilter()` 가 핵심이다.

![](https://raw.githubusercontent.com/Revi1337/BlogImageFactory/main/spring/SpringSecurity/DelegatingFilterProxy_FilterChainProxy/Pasted%20image%2020231220013825.png)

이 모든 Filter 들이 끝나야 비로소 Servlet(DispatcherServlet) 을 탈 수 있게 된다.

## 🔥 DEBUGGING 시작

자 이제 디버깅을 통해 앞서 설명한 과정들을 코드레벨에서 분석해보자.

### ✔ DelegatingFilterProxy 의springSecurityFilterChain 빈 조회

사용자의 요청이 들어오면 `DelegatingFilterProxy` 의 doFilter() 에서 가장 먼저 그 요청을 받는다. 곧바로 springSecurityFilterChain 빈을 찾기 위해  `initDelegate()` 를 호출한다.

![](https://raw.githubusercontent.com/Revi1337/BlogImageFactory/main/spring/SpringSecurity/DelegatingFilterProxy_FilterChainProxy/Pasted%20image%2020231220003242.png)


initDelegate() 메서드에서는 실제로 `ApplicationContext` . 즉 스프링 컨테이너에서 `springSecurityFilterChain`  이름의 Bean 을 찾아와 반환해주게 된다. 여기서 반환된 Bean 이 바로 FilterChainProxy 이다.

![](https://raw.githubusercontent.com/Revi1337/BlogImageFactory/main/spring/SpringSecurity/DelegatingFilterProxy_FilterChainProxy/Pasted%20image%2020231220003441.png)

### ✔ FilterChainProxy 에게 요청을 위임

springSecurityFilterChain 이름을 갖고있는 빈의 실제 객체인 FilterChainProxy 를 리턴받고 나면 `FilterChainProxy` 에게 사용자의 요청을 위임해주는 역할을 수행하는 `invokeDelegate()` 를 호출하게 된다.

![](https://raw.githubusercontent.com/Revi1337/BlogImageFactory/main/spring/SpringSecurity/DelegatingFilterProxy_FilterChainProxy/Pasted%20image%2020231220003646.png)


실제로 해당 메서드에서는 doFilter() 를 통해 `FilterChainProxy` 에게 사용자의 요청을 위임하고있는 것을 볼 수 있다.

![](https://raw.githubusercontent.com/Revi1337/BlogImageFactory/main/spring/SpringSecurity/DelegatingFilterProxy_FilterChainProxy/Pasted%20image%2020231220003833.png)

### ✔ 생성된 Filter 들을 조회

DelegatingFilterProxy 에게 사용자의 요청을 위임받은 FilterChainProxy 는 doFilterInternal() 안에서 Spring Security 의 초기화 시점에 만들어진 Filter 들의 조회, `VirtualFilterChain` 의 생성, VirtualFilterChain 을 실행하는 과정을 모두 수행하게 된다.

![](https://raw.githubusercontent.com/Revi1337/BlogImageFactory/main/spring/SpringSecurity/DelegatingFilterProxy_FilterChainProxy/Pasted%20image%2020231220004159.png)


바로 앞에서 말했듯이, doFilterInternal() 에서는 첫번째로 `getFilters()`  를 통해 Spring Security 초기화 시점에 생성된 FIlter 들을 조회하여 찾아오게 된다.

![](https://raw.githubusercontent.com/Revi1337/BlogImageFactory/main/spring/SpringSecurity/DelegatingFilterProxy_FilterChainProxy/Pasted%20image%2020231220004715.png)

### ✔ VirtualFilterChain 생성 및 실행

그리고 `this.filterChainDecorator.decorate()` 에 이전에 조회한 FIlter 들을 매개변수로 넘겨줌으로서, `VirtualFilterChain` 객체를 생성하고 VirtualFilterChain 의 doFIlter() 를 호출해준다.

![](https://raw.githubusercontent.com/Revi1337/BlogImageFactory/main/spring/SpringSecurity/DelegatingFilterProxy_FilterChainProxy/Pasted%20image%2020231220004856.png)

### ✔ 생성된 Filter 들을 실행

VirtualFilterChain 의 doFilter() 안에서는 이전에 조회한 Filter 들을 실행하게된다. 이때 Filter 들이 컬렉션에 담겨져있긴 하지만, 반복문을 통해서 실행되지 않는다.


![](https://raw.githubusercontent.com/Revi1337/BlogImageFactory/main/spring/SpringSecurity/DelegatingFilterProxy_FilterChainProxy/Pasted%20image%2020231220005618.png)


아래와 같이 첫번째 필터를 수행하고 나면 VirtualFilterChain 으로 다시 돌아와서 두번째 필터를 시작한다.

![](https://raw.githubusercontent.com/Revi1337/BlogImageFactory/main/spring/SpringSecurity/DelegatingFilterProxy_FilterChainProxy/Pasted%20image%2020231220015424.png)


이제 두번쨰 필터가 끝나면 VirtualFilterChain 으로 다시 돌아와 세번째 필터를 시작한다.

![](https://raw.githubusercontent.com/Revi1337/BlogImageFactory/main/spring/SpringSecurity/DelegatingFilterProxy_FilterChainProxy/Pasted%20image%2020231220015458.png)

이 과정을 필터가 끝날때 까지 반복한다.

### ✔ DispatcherServlet 진입

모든 필터가 에외 없이 잘 수행되게 되면, 드디어 DispatcherServlet 을 타게 된다.

![](https://raw.githubusercontent.com/Revi1337/BlogImageFactory/main/spring/SpringSecurity/DelegatingFilterProxy_FilterChainProxy/Pasted%20image%2020231220020522.png)


### ✔ 사용된 Breakpoints

![](https://raw.githubusercontent.com/Revi1337/BlogImageFactory/main/spring/SpringSecurity/DelegatingFilterProxy_FilterChainProxy/Pasted%20image%2020231220022412.png)

## 🔥 정리

1. DelegatingFilterProxy 는 단순히 FilterChainProxy 에게 사용자의 요청을 위임하는 역할을 한다.
2. FilterChainProxy 는 애플리케이션에 생성된 Filter 들을 조회하며, VirtualFitlerChain 을 만들며, VirtualFitlerChain 를 통해 생성된 Filter 들을 실행한다.
3. VirtualFitlerChain 를 통해 Filter 들이 실행될때는 반복문을 통해 실행되는 것이 아닌, 필터들이 끝날때마다 VirtualFitlerChain 로 돌아와서 다음 필터를 새롭게 호출하는 방식이다.
4. 필터들이 모두 끝나야 Servlet 이 호출된다.
