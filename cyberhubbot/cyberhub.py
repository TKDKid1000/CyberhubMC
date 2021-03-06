import discord
from discord.ext import commands
from discord.ext.commands import CommandNotFound
from discord.utils import get
import asyncio
import random
import time
import os
import requests
import json
import re
file = open("blocked-words.txt", "r")
content = file.read()
blocked_words = content.split(" ")
file.close()

def save(savemap, file):
     with open(file, "w") as f:
        json.dump(savemap, f)
def load(file):
    with open(file) as f:
        loadmap = json.load(f)
        return loadmap

class Bot(commands.Bot):
    def __init__(self):
        intents = discord.Intents.default()
        intents.members = True
        super(Bot, self).__init__(command_prefix=commands.when_mentioned_or("/"), intents=intents)
        self.load_extension("cogs.modCommands")
        self.load_extension("cogs.funCommands")
        self.load_extension("cogs.botCommands")
        self.load_extension("cogs.leveling")
    
    async def on_ready(self):
        print('Logged on as Cyberhub#3258')
        game = discord.Game("cyberhub.net")
        await self.change_presence(activity=game, status=discord.Status.online)

    async def on_command_error(self, ctx, error):
        if isinstance(error, CommandNotFound):
            return
        raise error

    async def on_member_join(self, member:discord.Member):
        channel = get(member.guild.channels, name="welcome")
        embed=discord.Embed(title="**Welcome to Cyberhub!**", description="Welcome " + member.mention + " to **Cyberhub!** They are member " + str(member.guild.member_count))
        embed.set_thumbnail(url=member.avatar_url)
        await channel.send(embed=embed)

    async def on_message(self, message):
            if message.author == self.user:
                return

            if any(x in message.content.lower() for x in blocked_words):
                await message.delete()

            if message.content.startswith("/"):
                tags = load("tags.json")
                tag = message.content.replace("/", "")
                if tag in tags:
                    await message.channel.send(tags[tag].replace("$author", message.author.mention))

            await self.process_commands(message)

with open("token.txt") as f:
    bot = Bot()
    bot.run(f.read())
