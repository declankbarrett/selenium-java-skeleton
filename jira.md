Summary: Search and filter users on the Users list
Type: Story · Labels: users, search, frontend, backend
User story As a project manager, I want to search and filter the users list, so that I can find the right people quickly without scrolling through every record.
Description The Users page currently loads every user from GET /users and has no way to narrow the list. This story adds a free-text search box (name, surname, email) and a Position filter to the Users list. The API supports the same search and filter through optional query parameters.
Acceptance criteria
1.
Search by text. Given users exist, when I type "anna" in the search box, then only users whose name, surname or email contains "anna" are shown. The match ignores case and finds partial text.
2.
Search by full name. Given the user "Anna Nowak" exists, when I search "anna nowak", then that user is shown.
3.
Filter by position. Given users with different positions exist, when I pick "Test Engineer" from the Position filter, then only users with that position are shown.
4.
Position options. The Position filter lists each existing position once, sorted alphabetically, with "All positions" selected by default.
5.
Search and filter together. When I enter search text and pick a position, only users that match both are shown.
6.
No results. When nothing matches, the table shows "No users match your search" instead of an empty table.
7.
Clear. When I click "Clear", the search text and filter reset and all users are shown again.
8.
Short delay on typing. Results update about 300ms after I stop typing, with no need to press Enter.
9.
Actions still work. View, Update and Remove still work on rows in filtered results. After a remove, the current search and filter stay applied.
10.
API: query parameters. GET /users?search=<text>&position=<value> returns 200 with the matching users. Both parameters are optional, so GET /users with neither returns everyone, as it does today.
11.
API: empty result. A query with no matches returns 200 with [], not 404.
12.
API: safe input. Search values containing ', %,  or SQL such as ' OR 1=1 -- are treated as plain text. They cause no error and don't return unexpected rows.
Technical notes
•
Backend:
◦
Add optional filters to users-queries.getUsers, using ILIKE across name, surname, email and name || ' ' || surname.
◦
Use parameterised queries ($1, $2). The existing queries build SQL by inserting values straight into the string, which allows SQL injection, so the new query mustn't do the same.
◦
Escape % and  in the search value.
•
Positions: add GET /users/positions, which returns the distinct positions for the dropdown.
•
Frontend: in users-list-scripts.js, add the search input, Position dropdown and Clear button. getAllUsers() should send the current search and filter values as query parameters.
•
Docs: update the Endpoints section of readme.md.
Out of scope: pagination, sorting, saved searches, and search on the Projects page.
Test considerations
•
API: each parameter alone, both together, neither, no matches, and special characters and SQL-injection strings.
•
UI end to end: typing, the short delay, filter + search, Clear, the no-results message, and removing a user while filtered.
•
Test data: the seed data has two users named "Jan Kowalski" with different positions. That makes it useful for checking search + filter together.
Estimate: 3–5 story points

