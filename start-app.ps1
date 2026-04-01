$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:Path += ";$env:JAVA_HOME\bin"
cmd.exe /c "C:\Users\USER\apache-maven-3.9.6\bin\mvn.cmd clean spring-boot:run"
