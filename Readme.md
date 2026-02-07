# Todo app
This is a simple proof of concept app for todos, inspired by 
Trello. There are a number of boards. Each board has a number of orderd lists.
Each list has a number of ordered todos.

## Purpose
The main purpose here is to tinker with ports and adapters architecture.

## Architectural decisions
* Different modules for application and infrastructure ensuring compile time dependency check
* Make illegal states unrepresentable
* Domain model immutable
* No "commands", use cases taks a list of arguments
* No spring boot in applicaton module
* Board is aggregate root pretty much everywhere
* Application inbound ports are not interfaces, but use case implemntatios. This is a short cut.

## Plan for use cases and endpoints
| Use Case | Description | Endpoint | Method |
|----------|-------------|----------|--------|
| List all boards | View all available boards in the system | /boards | GET |
| Create a board | Create a new board with a title and optional description | /boards | POST |
| Get board details | Retrieve a specific board with all its lists and cards | /boards/:id | GET |
| Update board | Modify board properties like title, description, or color | /boards/:id | PUT |
| Delete board | Remove a board and all its lists and cards | /boards/:id | DELETE |
| Create a list | Add a new list/column to a board | /boards/:boardId/lists | POST |
| Update list | Change a list's properties like its title | /lists/:id | PUT |
| Reorder list | Change a list's position within its board | /lists/:id/position | PUT |
| Delete list | Remove a list and all its cards | /lists/:id | DELETE |
| Create a card | Add a new task card to a list | /lists/:listId/cards | POST |
| Update card | Modify card properties like title, description, or due date | /cards/:id | PUT |
| Move/reorder card | Change a card's position within a list or move it to a different list | /cards/:id/position | PUT |
| Delete card | Remove a card from the system | /cards/:id | DELETE |
