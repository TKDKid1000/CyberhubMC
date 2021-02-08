from flask import Flask, request, render_template, flash, redirect, url_for
from flask_login import login_required, current_user

import os
import json
import random
from ..loaduser import loaduser

@login_required
def newforumpost():
    if request.method == "GET":
        return render_template("newforumpost.html", data=loaduser(current_user.id))
    print(request.method)
    title = request.form["title"]
    content = request.form["content"]
    username = loaduser(current_user.id)["username"]
    os.chdir("posts")
    post = {
        "title" : title,
        "content" : content,
        "author" : username,
        "comments" : {

        }
    }
    letterid = randLetters(10)
    print(letterid)
    with open(letterid+".json", "w") as f:
        json.dump(post, f)
    os.chdir("..")
    return redirect(url_for("viewforumpost", postid=letterid))

def randLetters(amount):
    name = ""
    if len(name)<amount:
        while len(name)<amount:
            name = name + random.choice("1234567890")
    return name

