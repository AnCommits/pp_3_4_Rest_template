package an.pp;

import an.pp.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

public class Main {
    private static final String URL = "http://94.198.50.185:7081/api/users";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws JsonProcessingException {
//--------------------------------------------------
// 1.   Получить список всех пользователей
        ResponseEntity<String> response1 = restTemplate.getForEntity(URL, String.class);
        String body1 = response1.getBody();
        List<User> users = objectMapper.readValue(body1, new TypeReference<>() {
        });
//--------------------------------------------------
// 2.   сохранить свой session id
        HttpHeaders headers1 = response1.getHeaders();
        String sessionId = Objects.requireNonNull(headers1.get("Set-Cookie")).get(0);
//--------------------------------------------------
// 3.   Сохранить пользователя с id = 3, name = James, lastName = Brown, age = на ваш выбор.
//      В случае успеха вы получите первую часть кода.
        User user = new User(3L, "James", "Brown", (byte) 3);
        HttpHeaders headers2 = new HttpHeaders();
        headers2.set("Cookie", sessionId);
        HttpEntity<User> entity = new HttpEntity<>(user, headers2);
        ResponseEntity<String> response2 = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
        String codePart1 = response2.getBody();
//--------------------------------------------------
// 4.   Изменить пользователя с id = 3. Необходимо поменять name на Thomas, а lastName на Shelby.
//      В случае успеха вы получите еще одну часть кода.
        user.setName("Thomas");
        user.setLastName("Shelby");
        HttpHeaders headers3 = new HttpHeaders();
        headers3.set("Cookie", sessionId);
        HttpEntity<User> entity3 = new HttpEntity<>(user, headers3);
        ResponseEntity<String> response3 = restTemplate.exchange(URL, HttpMethod.PUT, entity3, String.class);
        String codePart2 = response3.getBody();
//--------------------------------------------------
// 5.   Удалить пользователя с id = 3.
//      В случае успеха вы получите последнюю часть кода.
        HttpHeaders headers4 = new HttpHeaders();
        headers4.set("Cookie", sessionId);
        HttpEntity<User> entity4 = new HttpEntity<>(headers4);
        ResponseEntity<String> response4 = restTemplate.exchange(URL + "/3", HttpMethod.DELETE, entity4, String.class);
        String codePart3 = response4.getBody();
//--------------------------------------------------
        String code = codePart1 + codePart2 + codePart3;
        System.out.println(code.length());
        System.out.println(code);
    }
}
