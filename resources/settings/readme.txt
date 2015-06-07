When run in IDE, put line:
-Djava.security.manager -Djava.security.policy=resources/settings/security.policy
into VM options
Always run servers first:
Logger server: LoggerServer.java
Business server: Business.java
Broker server: Broker.java
Client: BrokerServiceClient.java