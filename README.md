# EDC Samples for eventing system

This repository provides examples of how to use the EDC connector event system which is explained in the [developer documentation](https://github.com/eclipse-edc/Connector/blob/main/docs/developer/events.md).

As source code, the repository [Samples](https://github.com/eclipse-edc/Samples) of Eclipse EDC has been cloned. To speed up the compilation process, only [Transfer Samples](https://github.com/eclipse-edc/Samples/blob/main/transfer/README.md) has been used, so the rest of the examples have been removed.

# Event sample

In this basic sample a service extension is created in the `ExampleEventExtension.java` file. Inside this extension the subscriber of the `ExampleEventSubscriber()` event is called. This subscriber is defined in the `ExampleEventSubscriber.java` file. For this sample, any event occurring in the connector will execute `ExampleEventSubscriber()` and the type of event that has produced its execution will be printed.

### 0. Prerequisites

Following the sample [Write your first extension](https://github.com/eclipse-edc/Samples/blob/main/basic/basic-02-health-endpoint/README.md), the `ExampleEventExtension.java` and `ExampleEventSubscriber.java` files have been created inside the `transfer/transfer-00-prerequisites/connector/src/main/java/org/eclipse/edc/sample/extension/event/` directory. The created extension has also been added to the `org.eclipse.edc.spi.system.ServiceExtension` file located in the `transfer/transfer-00-prerequisites/connector/src/main/resources/META-INF/services/` directory.

Clone the modified repository with the `event` tag

```bash
git clone -b event https://github.com/johnnychoque/edc-conn-modif.git
```
### 1. Build and run the modified connector

Run the commands of [Transfer sample 00: Prerequisites](https://github.com/eclipse-edc/Samples/blob/main/transfer/transfer-00-prerequisites/README.md) to build and execute the modified comector.

```bash
./gradlew transfer:transfer-00-prerequisites:connector:build
```

Execute the connector as Provider for initial testing

```bash
java -Dedc.keystore=transfer/transfer-00-prerequisites/resources/certs/cert.pfx -Dedc.keystore.password=123456 -Dedc.fs.config=transfer/transfer-00-prerequisites/resources/configuration/provider-configuration.properties -jar transfer/transfer-00-prerequisites/connector/build/libs/connector.jar
```
### 2. Test the modified connector

Following the sample [Transfer sample 01: Negotiation](https://github.com/eclipse-edc/Samples/blob/main/transfer/transfer-01-negotiation/README.md), open another terminal and create an asset on the Provider by running:

```bash
curl -d @transfer/transfer-01-negotiation/resources/create-asset.json \
  -H 'content-type: application/json' http://localhost:19193/management/v3/assets \
  -s | jq
```
The following message will be displayed on the terminal:

```bash
>>>>>>>>>>>>>> EVENT >>>>>>>>>>>>>
org.eclipse.edc.connector.controlplane.asset.spi.event.AssetCreated@7fb52601
```
