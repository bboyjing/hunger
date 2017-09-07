ps -ef | grep nginx | grep -v grep
if [ $? -ne 0 ]
then
    sudo /usr/local/Cellar/openresty/1.11.2.5/nginx/sbin/nginx -c /Users/zhangjing/IdeaProjects/hunger/conf/nginx.conf
    echo "nginx start"
else
    sudo /usr/local/Cellar/openresty/1.11.2.5/nginx/sbin/nginx -s reload
    echo "ngixn restart"
fi