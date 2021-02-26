from flask_login import current_user
from flask import render_template, request

import pymongo
import json
from ..loaduser import loaduser

def console():
    with open("currentnews.md") as f:
        news = f.read()
    if current_user.is_authenticated:
        with open("admin.json") as f:
            admins = json.load(f)
            if current_user.id in admins:
                if request.method == "GET":
                    return render_template("console.html", info="", admin="true", data=loaduser(current_user.id))
                command = request.form["cmd"]
                client = pymongo.MongoClient("mongodb://localhost")
                db = client["cyberhubwebcmd"]
                collection = db["commands"]
                command = {"command" : command}
                collection.insert_one(command)
                return render_template("console.html", info="Attempting to send the command to the server...", admin="true", data=loaduser(current_user.id))
            else:
               return render_template("console.html", admin="false", data=loaduser(current_user.id)) 
    else:
        return render_template("console.html", admin="false")