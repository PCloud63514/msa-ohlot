# WebApp Oh-Lot !!

### webapp-ohlot clone repository
```shell
$ git clone https://github.com/PCloud63514/webapp-ohlot.git {project-path}
$ cd project-path
$ git remote remove origin
```

### application run
> Java 11 must be installed.
```shell
$ ./gradlew applications:{app-name}:bootRun

// example
$ ./gradlew applications:app-ohlot:bootRun
```

### swagger 3.0
> can configure the port from [applications/app-ohlot/src/resources/application.yml](https://github.com/PCloud63514/webapp-ohlot/blob/main/applications/app-ohlot/src/main/resources/application.yml)
>
> [localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)