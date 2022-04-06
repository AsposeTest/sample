# PPTX to PDF Save Error: java.lang.reflect.InvocationTargetException
[https://forum.aspose.com/t/pptx-to-pdf-save-error-java-lang-reflect-invocationtargetexception/240115]
----------------------------
- I am using Aspose.total with *Developer OEM license*.

- With Aspose slides, I am trying to convert a PPTX file to PDF
~~~~~~~~~~~~~~~~~~~~~~~~~~
com.aspose.slides.LoadOptions loadOptions = new com.aspose.slides.LoadOptions();
Presentation pres = new Presentation(inputStream, loadOptions);
pres.save(outputStream, com.aspose.slides.SaveFormat.Pdf);
~~~~~~~~~~~~~~~~~~~~~~~~~~

- in [JDK - 1.8.0_231], the conversion works as expected in both local and server (Google Cloud VM instance)

- I am using the latest version of Aspose slides [21.12]
~~~~~~~~~~~~~~~~~~~~~~~~~~
 <dependency>
 	<groupId>com.aspose</groupId>
 	<artifactId>aspose-slides</artifactId>
 	<version>21.12</version>
 	<classifier>jdk16</classifier>
 </dependency>
~~~~~~~~~~~~~~~~~~~~~~~~~~

- in [OpenJDK  - 15], the conversion line throws the below error
--------------
[ERROR MSG] Handler dispatch failed; nested exception is java.lang.InternalError: java.lang.reflect.InvocationTargetException

[ERROR CAUSE] java.lang.InternalError: java.lang.reflect.InvocationTargetException

