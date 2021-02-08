from flask_login import current_user
from flask import render_template, request

import json
from ..loaduser import loaduser

def newspage():
    with open("currentnews.md") as f:
        news = f.read()
    if current_user.is_authenticated:
        with open("admin.json") as f:
            admins = json.load(f)
            if current_user.id in admins:
                if request.method == "GET":
                    return render_template("news.html", admin="true", news=news, data=loaduser(current_user.id))
                content = request.form["formnews"]
                with open("currentnews.md", "w") as f:
                    f.write(content)
                    return render_template("news.html", admin="true", news=news, data=loaduser(current_user.id))
            else:
               return render_template("news.html", admin="false", news=news, data=loaduser(current_user.id)) 
    else:
        return render_template("news.html", admin="false", news=news)