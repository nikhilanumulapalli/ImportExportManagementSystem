**Requirements**
scalaVersion: 2.11.12
sbt: 1.3.8

**Build**
Build the project using built.sbt

**Intital Setup**
If you running project for the first time, run  /src/main/scala/Data.scala
This inserts some sample products, customers and users into db running on the local host

**System**
To run the system excute the /src/main/scala/System/System.scala

`For login username and password for both customer and supplier is john, john` 

The csv path is set to Data directory in the project directory.
Format of csv file to insert multiple products 
`name, productPrice, sellingPrice, categories, manufactureNumber(could be anything), availability`