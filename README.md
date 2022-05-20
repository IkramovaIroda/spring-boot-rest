# spring-boot-restForOCTO

#Домашнее задание к занятию 7.2: REST
Необходимо выполнить и предоставить на проверку следующую задачу:

Сервис авторизации.
Продвинутый сервис авторизации (со звездочкой *).
=======

Инструкция по выполнению домашнего задания
Пишите код в IDE (рекомендуем Intellij IDEA, версия Community).
Почему лучше работать в IDE? — Ускоряет работу, есть подсветка ошибок, отладка по шагам.
Почему Intellij IDEA? — она бесплатная, умная, большинство разработчиков пользуются именно ей.
Опирайтесь на принятый стиль оформления кода.
Готовый код необходимо загрузить в специально созданный для данного домашнего задания репозиторий на сайте github.com.
На сайте github.com найдите только что созданный репозиторий. Перейдя в репозиторий, нажмите на зеленую кнопку CODE и скопируйте появившуюся ссылку.
В личном кабинете на сайте netology.ru в поле комментария к домашней работе вставьте скопированную ссылку и отправьте работу на проверку.
Никаких файлов прикреплять не нужно.

Задача Сервис авторизации
Описание
Сегодня мы с вами реализуем сервис авторизации пользователей по логину и паролю. Но ключевым в этом задании будет то, как ваше приложение будет реагировать на ошибки, которые наш сервис будет выбрасывать в разных случаях.

Для работы необходимо подготовить несколько классов:

Создайте spring boot приложение и все классы контроллеры, сервисы и репозитории сделать бинами в вашем application context.

Запрос на разрешения будет приходить на контроллер:

@RestController
public class AuthorizationController {
    AuthorizationService service;
    
    @GetMapping("/authorize")
    public List<Authorities> getAuthorities(@RequestParam("user") String user, @RequestParam("password") String password) {
        return service.getAuthorities(user, password);
    }
}
Класс-сервис, который будет обрабатывать введенные логин и пароль, выглядит следующим образом.
public class AuthorizationService {
    UserRepository userRepository;

    List<Authorities> getAuthorities(String user, String password) {
        if (isEmpty(user) || isEmpty(password)) {
            throw new InvalidCredentials("User name or password is empty");
        }
        List<Authorities> userAuthorities = userRepository.getUserAuthorities(user, password);
        if (isEmpty(userAuthorities)) {
            throw new UnauthorizedUser("Unknown user " + user);
        }
        return userAuthorities;
    }

    private boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private boolean isEmpty(List<?> str) {
        return str == null || str.isEmpty();
    }
}
Он принимает в себя логин и пароль и возвращает разрешения для этого пользователя, если такой пользователь найден и данные валидны. Если присланные данные неверны, тогда выкидывается InvalidCredentials:

public class InvalidCredentials extends RuntimeException {
    public InvalidCredentials(String msg) {
        super(msg);
    }
}
Если наш репозиторий не вернул никаких разрешений, либо вернул пустую коллекцию, тогда выкидывается ошибка UnauthorizedUser:

public class UnauthorizedUser extends RuntimeException {
    public UnauthorizedUser(String msg) {
        super(msg);
    }
}
Enum с разрешениями выглядит следующим образом:

public enum Authorities {
    READ, WRITE, DELETE
}
Код метод getUserAuthorities класс UserRepository, который возвращает либо разрешения, либо пустой массив, надо реализовать вам.
public class UserRepository {
    public List<Authorities> getUserAuthorities(String user, String password) {
        return ...;
    }
}
Для проверки работоспособности можно из браузера сделать следующий запрос, заполнив <ИМЯ_ЮЗЕРА> и <ПАРОЛЬ_ЮЗЕРА> своими тестовыми данными: localhost:8080/authorize?user=<ИМЯ_ЮЗЕРА>&password=<ПАРОЛЬ_ЮЗЕРА>

Теперь, когда весь код у вас готов, то вам необходимо написать обработчики ошибок, которые выкидывает сервис AuthorizationService. Требования к ним такие:
На InvalidCredentials он должен обратно клиенту отсылать http статус с кодом 400 и телом в виде сообщения из exception'а
На UnauthorizedUser он должен обратно клиенту отсылать http статус с кодом 401 и телом в виде сообщения из exception'а и писать в консоль сообщение из exception'а
  
  
  Задача Продвинутый сервис авторизации (со звездочкой *).
Описание
Задача простая: расширить функционал проедыдущего задания "Сервис авторизации"

Теперь ваш контроллер должен принимать не два объекта отдельно, а один объект содержащий значения user и password. Соответственно и AuthorizationService теперь работает с одним объектом. При этом, API для клиента не изменилось и он отправляет запрос такого вида localhost:8080/authorize?user=<ИМЯ_ЮЗЕРА>&password=<ПАРОЛЬ_ЮЗЕРА>. Также вы можете заметить, что вы также должны проверять объект на валидность с помощью аннотации @Valid. Подумайте, как вы должны валидировать поля объекта User:

@RestController
public class AuthorizationController {
    AuthorizationService service;
    
    @GetMapping("/authorize")
    public List<Authorities> getAuthorities(@Valid User user) {
        return service.getAuthorities(user);
    }
}
Сделать преобразование одного объекта в два вы можете с помощью своего HandlerMethodArgumentResolver и ,например, своей аннотации.
