# SpecificationTransfer
This project transfers comments and annotations from files of one java project to files of another project and is a reduced prototypical version of a student project by [@JonasKasper1881](https://github.com/JonasKasper1881)


## Usage
The Main-method of the Project requires three arguments in exactly this order: 
1. The path to the source directory of the project providing the annotations and comments.
2. The path to the source directory of the project which receives these annotations and comments.
3. The path to a directory, where the result should be stored.

## Assumptions: 
Currently, this is a research prototype and works with assumptions for the merge process:
- The package structure of both projects are the same
- The names of the java classes which should be merged are the same

## Limitations: 
The project currently does not support generects, overloaded methods or nested classes. 

