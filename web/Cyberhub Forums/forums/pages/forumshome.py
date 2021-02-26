from flask_login import current_user
from flask import render_template, url_for

import os
from ..loaduser import loaduser

def forumhomepage():
    sorted_posts = sorted(os.walk("path"))
    if current_user.is_authenticated:
        return render_template("forumshome.html", filename=getfiles("posts"), data=loaduser(current_user.id))
    else:
        return render_template("forumshome.html", filename=getfiles("posts"))

def getfiles(dirpath):
    a = [s for s in os.listdir(dirpath)
         if os.path.isfile(os.path.join(dirpath, s))]
    a.sort(key=lambda s: os.path.getmtime(os.path.join(dirpath, s)), reverse=True)
    return a
