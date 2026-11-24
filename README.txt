BBIT LIBRARY MANAGEMENT SYSTEM
==============================

Technology:
- Java SE / Swing / AWT
- ArrayList
- Java Serialization for file persistence
- No external libraries

Demo login:
Username: admin
Password: admin123

Project structure:
src/
  Main.java
  model/
  service/
  controller/
  view/
  exception/
  util/

Data:
library-data.ser is created automatically in the working directory.

JDK:
Use JDK 17 or newer.

Terminal:
From the project root:

Windows:
  javac -d out src\Main.java src\model\*.java src\service\*.java src\controller\*.java src\view\*.java src\exception\*.java src\util\*.java
  java -cp out Main

macOS/Linux:
  javac -d out src/Main.java src/model/*.java src/service/*.java src/controller/*.java src/view/*.java src/exception/*.java src/util/*.java
  java -cp out Main

IntelliJ IDEA:
1. Open the project folder.
2. Mark src as Sources Root if necessary.
3. Run Main.java.

Eclipse:
1. File > Import > Existing Projects into Workspace, or create a Java project and copy src.
2. Ensure src is a source folder.
3. Run Main.java as Java Application.

Apache NetBeans:
1. Create/open a Java project.
2. Add the supplied src package tree.
3. Run Main.java.

VS Code:
1. Install a JDK 17+ and the Extension Pack for Java.
2. Open the project folder.
3. Open Main.java and run it, or use the terminal commands above.

Notes:
- The application loads saved data at startup.
- Data is saved after library-changing operations.
- If library-data.ser is missing, a fresh empty library is started.
- If the file is corrupted, the application starts with empty data and attempts to rename the corrupted file to library-data.ser.corrupt.
- Sample data is only loaded into an empty library and never overwrites existing data.
