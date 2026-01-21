# Todo app
This is a simple proof of concept app for todos, inspired by 
Trello. There are a number of boards. Each board has a number of orderd lists.
Each list has a number of ordered todos.

## Purpose
The main purpose here is to tinker with ports and adapters architecture.

## Architectural decisions
* Make illegal states unrepresentable
* Domain model immutable
* No "commands", use cases taks a list of arguments
* No spring boot in applicaton module