# EDC Samples for eventing system

This repository provides examples of how to use the EDC connector event system which is explained in the [developer documentation](https://github.com/eclipse-edc/Connector/blob/main/docs/developer/events.md).

As source code, the repository [Samples](https://github.com/eclipse-edc/Samples) of Eclipse EDC has been cloned. To speed up the compilation process, only [Transfer Samples](https://github.com/eclipse-edc/Samples/blob/main/transfer/README.md) has been used, so the rest of the examples have been removed.

## Event sample

In this basic sample a service extension is created in the `ExampleEventExtension.java` file. Inside this extension the subscriber of the `ExampleEventSubscriber()` event is called. This subscriber is defined in the `ExampleEventSubscriber.java` file. For this sample, any event occurring in the connector will execute `ExampleEventSubscriber()` and the type of event that has produced its execution will be printed.

### 0. Prerequisites

Following the sample [Write your first extension](https://github.com/eclipse-edc/Samples/blob/main/basic/basic-02-health-endpoint/README.md), the `ExampleEventExtension.java` and `ExampleEventSubscriber.java` files have been created inside the `transfer/transfer-00-prerequisites/connector/src/main/java/org/eclipse/edc/sample/extension/event/` directory. The created extension has also been added to the `org.eclipse.edc.spi.system.ServiceExtension` file located in the `transfer/transfer-00-prerequisites/connector/src/main/resources/META-INF/services/` directory.

Clone the modified repository with the `event` tag

```bash
git clone -b event https://github.com/johnnychoque/edc-conn-modif.git
```

### 1. Build and run the modified connector

Run the commands of [Transfer sample 00: Prerequisites](https://github.com/eclipse-edc/Samples/blob/main/transfer/transfer-00-prerequisites/README.md) to build and execute the modified connector.

```bash
cd edc-conn-modif
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

## Sending http request when an event occurs

This sample extends the functionality of the event subscriber previously created by sending an http request to a test server every time an event occurs.

The `okhttp3` and `EdcHttpClient` libraries are used to create the http requests. The [EdcHttpClient](https://github.com/eclipse-edc/Connector/blob/5e210f90582512e85d6c6559c559c24acf1e7bde/spi/common/http-spi/src/main/java/org/eclipse/edc/http/spi/EdcHttpClient.java) library is a wrapper that uses okhttp3 to send the http requests in synchronous or asynchronous mode, but okhttp3 is also used to build the requests.

A new service extension `NegotiationEventExtention.java` and a new event subscriber `AgreementReached.java` are created. The `org.eclipse.edc.edc.spi.system.ServiceExtension` file is modified to include the new service extension `NegotiationEventExtension` instead of the previous `ExampleEventExtension`.

Inside the event subscriber `AgreementReached` is the `sendTestMessage()` method, which sends the http request to the test server running at `http://localhost:3000/`. Each time an event occurs the subscriber AgreementReached calls the sendTestMessage() method, which sends the JSON `(“access_token”, “token”)` to the server. 

### 0. Prerequisites

Clone the modified repository with the `request` tag

```bash
git clone -b request https://github.com/johnnychoque/edc-conn-modif.git
```
Build the modified comector.

```bash
cd edc-conn-modif
./gradlew transfer:transfer-00-prerequisites:connector:build
```
Install all the dependencies of test server

```bash
cd server-test
npm install
```

### 1. Test the modified connector

In a terminal run the connector as Provider for initial testing:

```bash
cd edc-conn-modif
java -Dedc.keystore=transfer/transfer-00-prerequisites/resources/certs/cert.pfx -Dedc.keystore.password=123456 -Dedc.fs.config=transfer/transfer-00-prerequisites/resources/configuration/provider-configuration.properties -jar transfer/transfer-00-prerequisites/connector/build/libs/connector.jar
```

In another terminal run the test server:

```bash
cd server-test
npm start
```

Open again another terminal and create an asset on the Provider:

```bash
curl -d @transfer/transfer-01-negotiation/resources/create-asset.json \
  -H 'content-type: application/json' http://localhost:19193/management/v3/assets \
  -s | jq
```
The following message will be displayed on the terminal:

```bash
>>>>>>>>>>>>>> EVENT >>>>>>>>>>>>>
org.eclipse.edc.connector.controlplane.asset.spi.event.AssetCreated@27a91331
{"access_token":"token"}
```

The terminal where the test server is running will also display the message `{“access_token”: “token”}`. This is the message sent by the http POST request of the sendTestMessage() method.
