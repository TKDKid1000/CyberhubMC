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
def save(savemap, file):
     with open(file, "w") as f:
        json.dump(savemap, f)
def load(file):
    with open(file) as f:
        loadmap = json.load(f)
        return loadmap

class botCommands(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @commands.command(brief="shows your latency")
    async def ping(self, ctx):
        await ctx.send(f"Pong! `{round(self.bot.latency*1000, 1)}ms`")

    @commands.command(brief="main command for the tag custom command system")
    async def tag(self, ctx, cmd=None, name=None, *, message=None):
        if cmd == None:
            await ctx.send('''Please enter a sub command!\nSub commands: \n/tag add <name> <message>\n/tag remove <name>\n/tag list''')
        else:
            tags = load("tags.json")
            if cmd == "add":
                if name != None and message != None:
                    if name in tags:
                        await ctx.send("That name already exists! Please pick a new one.")
                        return
                    else:
                        tags[name] = message
                        save(tags, "tags.json")
                        await ctx.send("Added " + name + " to the tags!")
                else:
                    await ctx.send("Please provide a name and message!")
            elif cmd == "remove":
                if name != None:
                    if not name in tags:
                        await ctx.send("That name does not exist! Please enter a new one.")
                        return
                    else:
                        del(tags[name])
                        save(tags, "tags.json")
                        await ctx.send("Removed " + name + " from the tags")
                else:
                    await ctx.send("Please provide a name!")
            elif cmd == "list":
                tags = load("tags.json")
                taglist = ""
                for tag in tags:
                    taglist = taglist + f"{tag}, "
                taglist = taglist[:-2]
                await ctx.send("Here are all of the tags:")
                await ctx.send(f"```{taglist}```")
            else:
                await ctx.send('''Please enter a valid sub command!
            Sub commands: 
            /tag add <name> <message>
            /tag remove <name>''')

    @commands.command(brief="the server info stuff")
    async def serverinfo(self, ctx):
        server = ctx.author.guild
        name = server.name
        members = server.member_count
        created = str(server.created_at).split(" ")[0]
        roles = len(server.roles)
        region = str(server.region)
        emojis = len(server.emojis)
        icon = server.icon_url
        owner = str(server.owner)
        embed=discord.Embed(title="Server Info", description="The important server info that nobody wants", color=0x1e00ff)
        embed.set_thumbnail(url=icon)
        embed.add_field(name="Name", value=name, inline=True)
        embed.add_field(name="Roles", value=roles, inline=True)
        embed.add_field(name="Owner", value=owner, inline=True)
        embed.add_field(name="Created", value=created, inline=True)
        embed.add_field(name="Region", value=region, inline=True)
        embed.add_field(name="Members", value=members, inline=True)
        embed.add_field(name="Emojis", value=emojis, inline=True)
        await ctx.send(embed=embed)

    @commands.command(brief="dms someone using the bot")
    async def message(self, ctx, member: discord.Member, *, content):
        channel = await member.create_dm()
        await channel.send(content)

    @commands.command(brief="reports a user")
    async def report(self, ctx, member: discord.Member=None, *, reason=None):
        if member == None or reason == None:
            await ctx.send("Please specify a member and a reason!")
            return
        reports = load("reports.json")
        reports.append([ctx.author.mention, member.mention, reason])
        save(reports, "reports.json")
        await ctx.send("Reported " + member.mention + " for " + reason)

def setup(bot):
    bot.add_cog(botCommands(bot))
