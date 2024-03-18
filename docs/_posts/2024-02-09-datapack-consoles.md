---
layout: post
title:  "Datapack Consoles"
categories: ["Tardis", "Specifics", "Datapacks", "Consoles"]
---

## Console Texture
- Make an console texture for any of the consoles that exist in the mod. You may use any of the variants as a base, but remember whatever you borrow as a base cannot be claimed as your own, used in any other media (without permission from Loqor), or distributed.
- Save this console texture to your datapack that is called by your own custom datapack namespace (see below), naming it whatever you like. but do remember to call it something you can remember, with **.png** as the image's format.

---

## Create a Datapack
- Follow the tutorial on the [Minecraft Wiki on datapacks](https://minecraft.wiki/w/Data_pack) on how to set one up, use the namespace from earlier

- Create a new **.json** file in the path

> `data/(namespace)/console/(console_texture_name).json`

- Inside this new .json file, paste

> ```json
> {
>   "id": "(namespace):(console_texture_name)",
>   "parent": "ait:console/(refer to example below)",
>   "texture": "(namespace):textures/(console_texture_name).png",
>   "emission": "(namespace):textures/(console_texture_name)_emission.png"
> }
> ```

- replacing the **namespace** and the **console_texture_name** with your own from earlier

- Now put this **datapack** into Minecraft.

## Create A Resource Pack
[Follow this guide](https://minecraft.wiki/w/Tutorials/Creating_a_resource_pack)

- Place your **.png** console texture in this path

`assets/(namespace)/console/(console_texture_name).png`

- Place your .**png** console texture emission in the same path

`assets/(namespace)/console/(console_texture_name)_emission.png`

- If you want people to be able to see your console texture variant, they will need this **resource pack**.

## Example Reference That's Easy to Follow :)
> This is just an example, do not use the ID "(namespace)" and make sure you're not using any parentheses. Those are just there for making sure you replace (namespace) with your own ID and (console_texture_name) with your texture's name.

Once you're finished, the json file should look like this:
```json
{
  "id": "(namespace):my_console",
  "parent": "ait:console/coral",
  "texture": "(namespace):textures/(console_texture_name).png",
  "emission": "(namespace):textures/(console_texture_name)_emission.png"
}
```
And your resourcepack directory should look like this:
```(namespace)/assets/textures/```

And once you make sure the textures are inside of the directory above, then you're ready to use your new console texture variant!

## Specifics About the Different Console Types
> - Hartnell Console = `hartnell`
> - Coral Console = `coral`
> - Copper Console = `copper`
> - Toyota Console = `toyota`
> - Alnico Console = `alnico`
> - Steam Console = `steam`
> > FYI: the `copper` console is currently unavailable.