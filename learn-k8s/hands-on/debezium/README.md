## Setting up MySQL

**1. Tạo Users:**

- Khi tạo connector với MySQL thì Debezium cần có user. Cái user này cần phải có quyền vào tất cả các databases trong MySQL.

  ```sql
  DROP USER 'debezium'@'%';

  CREATE USER 'debezium'@'%' IDENTIFIED BY 'debezium';
  GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'debezium'@'%';
  FLUSH PRIVILEGES;

  CREATE USER IF NOT EXISTS 'debezium'@'%' IDENTIFIED WITH mysql_native_password BY 'debezium';
  GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'debezium'@'%';
  FLUSH PRIVILEGES;
  ```

- Kiểm tra MySQL đã bật binlog hay chưa?
  ```sql
  SHOW VARIABLES LIKE 'log_bin';
  ```
- Cần chỉnh binlog_format = ROW
  ```log
  io.debezium.DebeziumException: The MySQL server is not configured to use a ROW binlog_format, which is required for this connector to work properly. Change the MySQL configuration to use a binlog_format=ROW and restart the connector.
  ```

**2. Tạo connectors:**

- List connector:

  ```powershell
  curl -i -X GET localhost:8083/connectors
  ```

- Delete connector:

  ```powershell
   curl -i -X DELETE localhost:8083/connectors/customers-connector
  ```

- Tao connector:

  ```powershell
  curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:8083/connectors/ -d '{
    "name": "customers-connector",
    "config": {
        "connector.class": "io.debezium.connector.mysql.MySqlConnector",
        "tasks.max": "1",
        "database.hostname": "dbzui-db-mysql",
        "database.port": "3306",
        "database.user": "debezium",
        "database.password": "debezium",
        "database.server.id": "184059",
        "database.server.name": "fullfillment",
        "table.include.list": "inventory.customers",
        "database.history.kafka.bootstrap.servers": "PLAINTEXT://dbzui-kafka:9092",
        "database.history.kafka.topic": "schema-changes.inventory",
        "debezium.source.database.history": "io.debezium.relational.history.KafkaDatabaseHistory",
        "schema.history.internal.kafka.topic": "schema-history",
        "schema.history.internal.kafka.bootstrap.servers": "dbzui-kafka:9092",
        "topic.prefix": "debezium"
    }
  }'
  ```

## Problem I: Rolled Over Bin-logs

```sql
SHOW BINLOG EVENTS IN 'mysql-bin.000004';

SHOW BINARY LOGS;
```

https://levelup.gitconnected.com/fixing-debezium-connectors-when-they-break-on-production-49fb52d6ac4e

https://thedataguy.in/debezium-mysql-snapshot-from-read-replica-and-resume-from-master/

https://thedataguy.in/build-production-grade-debezium-with-confluent-kafka-cluster/

https://www.thegeekstuff.com/2017/08/mysqlbinlog-examples/
