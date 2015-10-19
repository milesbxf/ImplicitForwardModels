#A biologically plausible forward model for active inference motor control

An MSc thesis submitted for MSc Evolutionary and Adaptive Systems at [University of Sussex, Brighton](http://www.sussex.ac.uk/).

I explored the advantages of using a free energy minimisation (FEM) model of motor control
rather than traditional optimal control techniques; I then argued how an existing evolutionary
robotics model demonstrated FEM motor control, and devised an experiment to see if a
biologically plausible implementation would work, using artificial neural networks. I used
evolutionary search to optimise these networks, providing insights into unsupervised system
analysis, and I found that artificial neural networks have potential for use in the evolutionary
robotics model of FEM motor control. I built an optimised and rapid Java framework for
evolutionary search and artificial neural net simulation with concurrency, and a task scheduler
allowing me to run many experiments overnight. Data analysis was performed with Python.

[Link to thesis (PDF)](https://dl.dropboxusercontent.com/u/47395591/Uni%20Projects/An_implicit_free_energy_minimising_motor_control_algorithm.pdf)



This package contains all source code, compiled binaries and experimental data for my MSc thesis. 

##Folders
/Analysis/
 - contains all the experimental data output and a single IPython notebook demonstrating the analysis.
 - the Implicit forward models.ipynb is the actual notebook, will require IPython Notebook (using Anaconda or other scientific Python distribution): [view using GitHub](https://github.com/mbryantlibrary/ImplicitForwardModels/blob/master/Analysis/Implicit%20forward%20models.ipynb)
 - Alternatively a HTML and PDF files of the notebook are in the same directory
 - A runnable .py file which contains all the source from the notebook is also included


/src/ - contains the main Java source code.

/test/ - contains Java JUnit tests.

/lib/ - contains required libraries

/bin/ - Java .class files and a packaged .jar

#To run

To run the main project, a compiled and packaged .jar file is available under /bin/. Either double click run.bat, or open a terminal window, navigate to the /bin/ folder and type the command 'java -jar MB_EASy_Project.jar'.

A window will come up; parameters are on the left, and the right shows currently running tasks. Enter a name, and select parameters, and click the 'Add' button at the bottom. The console window will display the calculations of initial fitness and run the GA. Progress is updated on the GUI. More tasks can be added later. Data is output to the /experiments/ folder as CSV for further analysis.
