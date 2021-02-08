from flask import Flask, request, render_template, flash, redirect, url_for
from flask_login import login_required, current_user

import os
import json
import random
from ..loaduser import loaduser

def viewforumpost(postid):
    os.chdir("posts")
    with open(postid+".json") as f:
        post = json.load(f)
    os.chdir("..")
    if request.method == "GET":
        if current_user.is_authenticated:
            return render_template("viewforumpost.html", posttitle=post["title"], postcontent=post["content"], postauthor=post["author"], comments=post["comments"], data=loaduser(current_user.id))
        else:
            return render_template("viewforumpost.html", posttitle=post["title"], postcontent=post["content"], postauthor=post["author"], comments=post["comments"])
    post["comments"][randLetters(10)] = {
        "author" : loaduser(current_user.id)["username"],
        "content" : request.form["comment"]
    }
    os.chdir("posts")
    with open(postid+".json", "w") as f:
        json.dump(post, f)
    os.chdir("..")
    return render_template("viewforumpost.html", posttitle=post["title"], postcontent=post["content"], postauthor=post["author"], comments=post["comments"], data=loaduser(current_user.id))

def randLetters(amount):
    name = ""
    if len(name)<amount:
        while len(name)<amount:
            name = name + random.choice("1234567890")
    return name
