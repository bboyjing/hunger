-- 引入healthcheck模块
local hc = require "resty.upstream.healthcheck"
healthcheck = function()
    local ok, err = hc.spawn_checker {
            shm = "healthcheck",
            upstream = "real_server",
            type = "http",
            http_req = "GET / HTTP/1.0\r\nHost: real_server\r\n\r\n",
            interval = 2000,
            timeout = 5000,
            fall = 3,
            rise = 2,
            valid_statuses = {200, 302},
            concurrency = 1,
        }
 
        if not ok then
            ngx.log(ngx.ERR, "=======> failed to spawn health checker: ", err)
            return
        end
end

-- 调用上面定义的function
healthcheck()