package org.github.http.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: RestWebController
 * @Description:
 * @author: <p> 雅诗兰黛  熬夜不怕黑眼圈</p>
 * @date 2020-09-08 22:00
 * @Copyright: 墨铭琦妙代码研究中心
 */
@RestController
@RequestMapping(value = "/flux")
public class RestWebController {

    @GetMapping(value = "/int")
    public Object response(){
        return Integer.MAX_VALUE;
    }
}

