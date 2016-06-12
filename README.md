Eva Arevalo
103062181

# SoftwareStudioAssignment8

Below is a description of the contents of essential files:

AndroidManifest.xml
I added the  <uses-permission android:name="android.permission.INTERNET" /> 
to allow for connectivity.

activity_main.xml
Contains the layout for the calculator, with 2 edit text fields for the operators,
4 buttons for the operator buttons. The id's are later used in the MainActivity.java file.
Uses linear layout.

ip_page.xml
Contains layout for the ip page. Contains an edit text field for the ip and a send button.
The id's are later used in the MainActivity.java file. Uses linear layout.

result_page.xml
Contains the result of the operation inputted by the user in the calculator. Contains a text
field for the result and a return button.
The id's are later used in the MainActivity.java file. Uses linear layout.

Ass8Server.java
Creates a server socket in port 2000 using the java.net.ServerSocket library with a small JFrame
GUI with a scrollable text area to display the messages received by the derver.
The Ip of the server is set using the following statement: static getLocalHost() method of the 
InetAdress class.
A forever loop creates a new Connection thread for each new client and runs it.
The constructor for connections sets up a Buffered reader and a PrintWriter for each connection.
they are connected to the sockets' input and output stream respectively.
Each connection thread implements a forever loop that reads the input stream of the socket (here,
the reader set up in the constructor) and appends its contents to the SErver's GUI text area.

MainActivity.java
Each layout has a function to transition to said state, jumpToIPLayout(), jumpToCalculatorLayout(),
and jumpoToResultLayout.
We use the onCreate(Bundle savedInstanceState) method to call the jumIPLayout function (after calling
the super constructor for the bundle).
The jumpToIPLayout() function sets the content view to ip_page.xml, finds the text area and button
using the ids on the xml file and sets a listener for the button, which gets the text from the id of 
the text field on the xml file and converts it to string before calling the jumpTpCalculatorLayout() 
method.
The jumpToCalculatorMethod begins by setting the Content View to the activity_main.xml file, which
contains the calculator layout. It then goes on finding by ID the buttons and textfields from the xml file and 
adds a listener to them. The listener is defined externally for all of the buttons (it's not 
defined internally as the rest of the buttons on this app). The action it performs depends on the 
id of the view the onClick function receives as an argument. The numbers from the edit text fields 
are gotten from the textfield and parsed to floats and the operation is applied to them and stored
in an internal variable. We call jumpToResultLayout() and create a new thread with the string
concatenation of the numbers from the textfields, the operator and the result and started.
The class thread is an inner class that extends Thread. its constructor takes a string and stores it
as a local variable. When the thread is started, it calls the inner run() method (after scheduling, etc.),
A new socket is created with the ip provided by the user. we use the Socket class' method
getOutputStream() to get the socket's output stram and use an OutputStram alias instantiated locally.
We create a Byte array sendStrByte from the message using the getBytes() method. Then, we pass it as 
argument calling the Output Stream's write method. We then flush and close it.
The jumpToResultLayout() method sets the content view to the result_page.xml file, finds the button 
and text field by id in the XML file and sets a listener for the button that calls the 
jumpToCalculatorLayout() method.

Problems

The server initially wouldn't receive the client's messages until losing the app, even after using the flush() method.
I closed the connection thread from the client socket and it solved the issue.

