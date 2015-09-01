This package contains all source code, compiled binaries and experimental data for the project.

Folders:

/Analysis/ - contains all the experimental data output and a single IPython notebook demonstrating the analysis.
	- the Implicit forward models.ipynb is the actual notebook, will require IPython Notebook (using Anaconda or other scientific Python distribution), or an online version can be viewed without at http://nbviewer.ipython.org/github/mbryantlibrary/ImplicitForwardModels/blob/master/Analysis/Implicit\%20forward\%20models.ipynb .
	- Alternatively a HTML and PDF files of the notebook are in the same directory
	- A runnable .py file which contains all the source from the notebook is also included


/src/ - contains the main Java source code.
/test/ - contains Java JUnit tests.
/lib/ - contains required libraries
/bin/ - Java .class files and a packaged .jar

To run the main project, a compiled and packaged .jar file is available under /bin/. Either double click run.bat, or open a terminal window, navigate to the /bin/ folder and type the command 'java -jar MB_EASy_Project.jar'.

A window will come up; parameters are on the left, and the right shows currently running tasks. Enter a name, and select parameters, and click the 'Add' button at the bottom. The console window will display the calculations of initial fitness and run the GA. Progress is updated on the GUI. More tasks can be added later. Data is output to the /experiments/ folder as CSV for further analysis.
