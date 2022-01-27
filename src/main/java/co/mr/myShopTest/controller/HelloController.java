package co.mr.myShopTest.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HelloController {
    @GetMapping("/hello")
    public String hello(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }

    @GetMapping("/ex01")
    public String ex01(Model model) {
        model.addAttribute("name", "<b>홍길동</b>");
        return "ex01";
    }

    @GetMapping("/var")
    public String var(Model model) {
        Person p1 = new Person("김길동", 10, "서울");
        Person p2 = new Person("고길동", 20, "대전");

        List<Person> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);

        Map<String, Person> map = new HashMap<>();
        map.put("p1", p1);
        map.put("p2", p2);

        model.addAttribute("person", p1);
        model.addAttribute("persons", list);
        model.addAttribute("personMap", map);

        return "var";
    }
    @GetMapping("/link")
    public String link(Model model) {
        model.addAttribute("param1", "value1");
        model.addAttribute("param2", "value2");

        return "link";
    }

    @GetMapping("/literal")
    public String literal(Model model) {
        model.addAttribute("str", "thymeleaf");
        return "literal";
    }

    @GetMapping("/operation")
    public String operation(Model model) {
        model.addAttribute("value", "thymeleaf");
        model.addAttribute("nullValue", "null");
        return "operation";
    }

    @GetMapping("/each")
    public String each(Model model) {
        person(model);
        return "/each";
    }
    private void person(Model model) {
        List<Person> list = new ArrayList<>();
        list.add(new Person("강길동", 10, "서울"));
        list.add(new Person("고길동", 20,"대전"));
        list.add(new Person("홍길동", 30,"부산"));
        model.addAttribute("people", list);
    }

    @GetMapping("/if")
    public String ifstate(Model model) {
        person(model);
        return "/if";
    }

    @GetMapping("/block")
    public String block(Model model) {
        person(model);
        return "block";
    }

    @GetMapping("/javascript")
    public String javascript(Model model) {
        model.addAttribute("person", new Person("kim", 10, "서울"));
        person(model);
        return "js";
    }


    @Data
    @AllArgsConstructor
    static class Person {
        private String name;
        private int age;
        private String address;
    }
}

