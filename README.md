# Sorting-Tasks
## About:
This project's goal is to find a valid ordering of a number of tasks using a directed graph. It parses a .task file, that will be specified on the command line. A file of n lines will define n different tasks. Each line of the file is a tab-separated list, where the first token is some new task, and all remaining tokens are the other tasks that must be done prior to it. Every listed task in the file will be the first task on exactly one line, and no task is directly dependent on itself. This program first determines whether or not there are tasks that are mutually dependent. It will output the answer to that question. When no tasks are co-dependent, the program will then output a numbered list which sorts the tasks into a valid order. However, some .task files will contain mutual dependencies. If several tasks are dependent on each other (either directly or indirectly), then they must be done at the same time. In this case, some numbered items will be comma-separated lists.

## Author:
Colin Monaghan
