<!DOCTYPE html>
<html>
    <head lang="en">
        <meta charset="UTF-8">
        <title>PwC Auth</title>
    </head>
    <body>
        <h1>PwC Auth</h1>

        <p>Hello
            <%
              out.println(session.getAttribute("name") + " " + session.getAttribute("surname"));
            %>
        </p>
        <p>You work at:
            <%
                out.println(session.getAttribute("groupName"));
            %>
        </p>
    </body>
</html>