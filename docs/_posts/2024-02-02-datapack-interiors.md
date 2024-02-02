---
layout: post
title:  "Datapack Desktops"
categories: ["Tardis", "Specifics", "Datapacks", "Desktop"]
---

## Preview Photo
1. Using whatever arts and crafts image editor you like (I recommend **paint.net** or **Gimp**), create a photo for your desktop in a *SQUARE* format.

2. Save this image in the **.png** image format, and with the name of your desktop.

If my desktop was called **"war"**, I'd save it as **"war.png"**.

3. Reuse this desktop name later, as it will also be the ID for your desktop.

---

## Converting Your Beautiful Build To NBT
Use a **Structure Block** to save your interior.

First save your interior, and note down your namespace (before the colon) and desktop name (after the colon)
The *.nbt* will be saved to ```.minecraft/saves/(WorldName)/generated/(namespace)/structures```.

---

## Create a Datapack
1. Follow the tutorial on the [Minecraft Wiki on datapacks](https://minecraft.wiki/w/Data_pack) on how to set one up, use the namespace from earlier

2. Place the .nbt file from earlier in

```data/(namespace)/structures/interiors/(desktop_name).nbt```

3. Create a new **.json** file in the path

```data/(namespace)/desktop/(desktop_name).json```

4. Inside this new .json file, paste

```"id": "namespace:desktop_name"```

5. replacing the namespace and the **desktop_name** with your own from earlier

6. Now put this **datapack** into Minecraft.

---

## Create A Resource Pack
[Follow this guide](https://minecraft.wiki/w/Tutorials/Creating_a_resource_pack)

1. Place your **.png** preview in the path

```assets/(namespace)/textures/desktop/(desktop_name).png```

2. If you want people to be able to see your preview, they will need this **resource pack**.