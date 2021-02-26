from flask import Flask, request, render_template, flash, redirect, url_for
from flask_login import login_required, current_user
import flask_login
import os
import json
import random
from ..loaduser import loaduser

@login_required
def accountpage():
    if request.method == "GET":
        return render_template("account.html", data=loaduser(current_user.id))
    if request.form["name"].strip() == "":
        name = loaduser(current_user.id)["username"]
    else:
        name = request.form["name"]
    if request.form["password"].strip() == "":
        password = loaduser(current_user.id)["password"]
    else:
        password = request.form["password"]
    if request.form["icon"].strip() == "":
        icon = loaduser(current_user.id)["icon"]
    else:
        icon = request.form["icon"]
    with open("accounts.json", "r") as f:
        users = json.load(f)
        del(users[current_user.id])
        users[current_user.id] = {"username" : name, "password" : password, "icon" : icon}
        with open("accounts.json", "w") as f:
            json.dump(users, f)
            
    return redirect(url_for('homepage'))