package cn.didadu;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangjing on 2017/8/25.
 */
@RestController
@RequestMapping
public class RealServerController {
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String handle() {
        return "from real server 1";
    }

    @RequestMapping(path = "/{uri}", method = RequestMethod.GET)
    public String handleUri(ServletResponse response, @PathVariable String uri) {
        return "from real server 1, param is " + uri;
    }
}
