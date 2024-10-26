package org.lsh.jpademo.controller;

import lombok.extern.log4j.Log4j2;
import org.lsh.jpademo.dto.SampleDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.fasterxml.jackson.databind.cfg.CoercionInputShape.Array;

@Controller
@Log4j2
public class SampleController {
    @GetMapping("/hello")
    public void hello(Model model) {
        log.info("hello");
        model.addAttribute("msg", "hello world!");
    }
    @GetMapping("/ex/ex1")
    public void ex1(Model model) {
        log.info("ex1");
        List<Integer> list = Arrays.asList(1,2,3,4,5);
        model.addAttribute("list", list);
    }
    @GetMapping("/hello1")
    public void hello1(@RequestParam("name") String name,@RequestParam("age") int age, Model model) {
        log.info(name);
        model.addAttribute("name", name);
        model.addAttribute("age", age);
        model.addAttribute("msg", "hello 1");
    }
    @GetMapping("/hello2")
    public void hello2(@RequestParam("name") String name,
                       @RequestParam("age") int age,
                       Model model) {
        log.info("name"+name);
        log.info("age"+age);
        model.addAttribute("msg", "hello 2");
    }
    @GetMapping("/hello3")
    public void hello3(Model model) {
        model.addAttribute("msg", "hello 3");
    }
    @GetMapping("/ex/ex2")
    public void ex2(Model model) {
        log.info("ex2");
        List<String> strList2 = new ArrayList<>();
        List<String> strList = IntStream.range(1,10)
                .mapToObj(i->"Data"+i)
                .collect(Collectors.toList());
        for (int i=1; i<10; i++){
            strList2.add("Data"+i);
        }
        Map<String, Integer> maps=new HashMap<>();
        maps.put("홍길동",80);
        maps.put("박경미",75);
        maps.put("윤요섭",85);
        model.addAttribute("maps",maps);
        SampleDTO sampleDTO = new SampleDTO();
        sampleDTO.setName("hong");
        sampleDTO.setAge(20);
        sampleDTO.setGender("남자");

        model.addAttribute("sampleDTO", sampleDTO);
        model.addAttribute("strList", strList);
        model.addAttribute("strList2", strList2);
    }
    @GetMapping("/ex/ex3")
    public void ex3() {
        log.info("ex3");
    }
}
