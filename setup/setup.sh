 sudo rabbitmqctl add_user admin 123456
 sudo rabbitmqctl set_permissions -p "/" admin ".*" ".*" ".*"
mysql -u admin -p  -e  'source  init.sql'
