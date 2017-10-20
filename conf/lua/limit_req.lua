local limit_req = require("resty.limit.req")
-- 固定平均速率为2r/s，也就是500毫秒一个请求
local rate = 2
-- 桶容量
local burst = 3
local error_status = 503
-- 是否需要不延迟处理
local nodelay = false
-- 共享字典
local lim, err = limit_req.new("limit_req_store", rate, burst)
if not lim then
    ngx.exit(error_status)
end

-- IP维度的限流
local key = ngx.var.binary_remote_addr
local delay, err = lim:incoming(key, true)

-- 超出桶大小
if not delay and err == "rejected" then
    ngx.exit(error_status)
end

if delay > 0 then
    if not nodelay then
        -- 直接突发处理
    else
        -- 延迟处理
        ngx.sleep(delay)
    end
end