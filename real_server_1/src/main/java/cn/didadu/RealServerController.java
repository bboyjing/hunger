package cn.didadu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
@Scope("prototype")
public class RealServerController {

    @Autowired
    private RealServiceCommand realServiceCommand;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String handle() {
        return "from real server 1." + realServiceCommand.execute();
    }

    @RequestMapping(path = "/{uri}", method = RequestMethod.GET)
    public String handleUri(ServletResponse response, @PathVariable String uri) {
        return "from real server 1, param is " + uri;
    }
}
