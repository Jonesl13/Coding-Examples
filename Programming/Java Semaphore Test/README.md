# Project Description
This project was done to learn more about implementing semaphore systems in Java. In this program X users add elements onto a single buffer object, which are removed by Y
webservers. The number of users, webservers and elements are all specified by user input. The program automatically divides the number of inputted elements evenly across all users (even
for odd values), while webservers attempt to remove elements from the buffer as evenly as possible.The action of adding and removing elements from the buffer is performed concurrently, and the program also continues to operate even if the buffer is empty or full, with the user or webserver waiting until the buffer is again available. To run the program, run the “startServer.java” file. 
