import json

def loaduser(email):
    with open("accounts.json") as f:
        return json.load(f)[email]

def loadusers():
    with open("accounts.json") as f:
        return json.load(f)