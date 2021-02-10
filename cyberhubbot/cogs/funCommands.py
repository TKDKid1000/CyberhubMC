import discord
from discord.ext import commands
from discord.ext.commands import CommandNotFound
from discord.utils import get
import asyncio
import random
import time
import os
import requests
import base64
import json
import re
def save(savemap, file):
     with open(file, "w") as f:
        json.dump(savemap, f)
def load(file):
    with open(file) as f:
        loadmap = json.load(f)
        return loadmap
        
randomfile = open("random-words.txt", "r")
randomcontent = randomfile.read()

random_words = randomcontent.split(" ")
randomfile.close()

class funCommands(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @commands.command(brief="execute a python script in discord")
    async def python(self, ctx, *, script=None):
        if script != None:
            script = script.replace("```python", "")
            script = script.replace("```py", "")
            script = script.replace("```", "")
            script = script.replace("stdin", "")
            script = script.replace("input(", "")
            script = script.replace("open(", "")
            script = script.replace("import discord", "")
            script = script.replace("import os", "")
            script = script.replace("import subprocess", "")
            with open("script.py", "w") as f:
                f.write(script)
            stream = os.popen('''python3 script.py''')
            output = stream.read()
            if output == "":
                output = "No output."
            embed=discord.Embed(title="Python Script", description='```%s```' % output)
            await ctx.send(embed=embed)
        else:
            embed=discord.Embed(title="Python Script", description='''Run python in discord!
    This is intended to be a simple to use, lightweight python script executor. You can run anything from a simple hello world to advanced computing!
    Some features are disabled, such as standard input, exiting, and opening files.
    All other python 3.8 features have full functionality. Have fun!
    Code Blocks:
    Code blocks are a simple and awesome feature that lets you make your code neater!
    When running this command (/python <script>) Use **```python** for your code. (make sure to end it with ``` too!)
    This will allow you to use new lines without pressing shift enter, will enable pressing tab, and color coding your code!''')
            await ctx.send(embed=embed)

    @commands.command(brief="returns the MeMeY version of a text")
    async def memey(self, ctx, *, text):
        out = ""
        tlist = []
        tlist.extend(text)
        for letter in tlist:
            caps = random.randint(0, 1)
            if caps == 1:
                letter = letter.upper()
            else:
                letter = letter.lower()
            out = out+letter
        await ctx.send(out)
    
    @commands.command(brief="shows the skin of any minecraft java player")
    async def mcskin(self, ctx, ign):
        uuid = json.loads(requests.get("https://api.mojang.com/users/profiles/minecraft/%s" % ign).text)["id"]
        data = json.loads(requests.get("https://sessionserver.mojang.com/session/minecraft/profile/%s" % uuid).text)
        base64_message = data["properties"][0]["value"]
        base64_bytes = base64_message.encode('ascii')
        message_bytes = base64.b64decode(base64_bytes)
        message = message_bytes.decode('ascii')
        embed=discord.Embed(title="%s's Skin" % ign, url="https://namemc.com/profile/%s.1" % ign)
        skindata = json.loads(message)
        embed.set_thumbnail(url=skindata["textures"]["SKIN"]["url"])
        await ctx.send(embed=embed)

    @commands.command(brief="a type speed game")
    async def speedtype(self, ctx, amount=10):
        await ctx.send("You will have 60 seconds to type the text")
        await ctx.send("Please don't copy paste")
        await ctx.send("3")
        time.sleep(1)
        await ctx.send("2")
        time.sleep(1)
        await ctx.send("1")
        time.sleep(1)
        await ctx.send("GO!")
        starttime = time.time()
        def check(author):
            def inner_check(message):
                if ctx.author == author:
                    return True
                else:
                    return False
            return inner_check
        words = 0
        correct = 0
        while True:
            word = random.choice(random_words)
            await ctx.send(f"Type this: `{word}`")
            msg = await self.bot.wait_for('message', check=check)
            youtyped = msg.content
            await ctx.send(f'You typed: `{youtyped}`')
            if youtyped == word:
                correct += 1
            words += 1
            if words >= amount:
                break
        stoptime = time.time()
        fulltime = stoptime-starttime
        fulltime = round(fulltime)
        await ctx.send(f"You took: **{fulltime}** seconds!")
        await ctx.send(f"You got **{correct}** words correct!")
    
    @commands.command(brief="Create a vote poll!")
    async def createpoll(self, ctx, name=None, *, questions=None):
        polls = load("polls.json")
        if name == None:
            await ctx.send("Please specify a poll name and questions!")
            return
        else:
            if name in polls:
                await ctx.send("A poll with that name is already up!")
                return
            else:
                if questions == None:
                    await ctx.send("Please specify some questions! (seperated by `:`)")
                    return
                else:
                    questiondict = {}
                    for q in questions.split(":"):
                        questiondict[q.lower().strip()] = 0
                    polls[name] = {
                        "questions" : questiondict,
                        "owner" : ctx.author.id,
                        "who" : []
                    }
                    save(polls, "polls.json")
                    description = "To vote on the poll type /vote " + name + " (question). Simply copy paste the question to choose it.\n\n"
                    for q in questions.split(":"):
                        description = description + q + "\n"
                    embed=discord.Embed(title="**%s**" % name, description=description)
                    embed.set_author(name=ctx.author.mention, icon_url=ctx.author.avatar_url)
                    await ctx.send(embed=embed)
    
    @commands.command(brief="Answer a vote poll")
    async def vote(self, ctx, poll=None, *, question=None):
        polls = load("polls.json")
        if poll == None:
            await ctx.send("Please specify a poll name and answer!")
            return
        else:
            if poll in polls:
                if question.lower().strip() in polls[poll]["questions"]:
                    if ctx.author.id in polls[poll]["who"]:
                        await ctx.send("You already answered this poll!")
                        return
                    else:
                        polls[poll]["who"].append(ctx.author.id)
                        polls[poll]["questions"][question] = polls[poll]["questions"][question]+1
                        await ctx.send("You voted for " + question + "!")
                        save(polls, "polls.json")
                else:
                    await ctx.send("That answer does not exist! Try copy pasting it.")
                    return
            else:
                await ctx.send("That poll does not exist!")
                return
    
    @commands.command(brief="End a vote poll")
    async def endpoll(self, ctx, poll=None):
        polls = load("polls.json")
        if poll == None:
            await ctx.send("Please specify a poll name! (that you own)")
            return
        else:
            if poll in polls:
                if polls[poll]["owner"] == ctx.author.id:
                    description = "Here are the results of the poll by " + ctx.author.mention + ":\n"
                    for result in polls[poll]["questions"]:
                        description = description + "**" + result + ", " + str(polls[poll]["questions"][result]) + "**\n" 
                    embed=discord.Embed(title="**%s Results**" % poll, description=description)
                    embed.set_author(name=ctx.author.mention, icon_url=ctx.author.avatar_url)
                    await ctx.send(embed=embed)
                    polls.pop(poll)
                    save(polls, "polls.json")
                else:
                    await ctx.send("You can't end a poll you don't own!")
            else:
                await ctx.send("That poll does not exist!")

def setup(bot):
    bot.add_cog(funCommands(bot))
