---
layout: post
title:  "Datapack Exteriors"
categories: ["Tardis", "Specifics", "Datapacks", "Exteriors"]
---

## Exterior Texture
1. Make an exterior texture for any of the exteriors that exist in the mod. You may use any of the variants as a base, but remember whatever you borrow as a base cannot be claimed as your own, used in any other media (without permission from MDTeam), or distributed.
2. Save this exterior texture to your datapack that is called by your own custom datapack namespace (see below), naming it whatever you like. but do remember to call it something you can remember, with **.png** as the image's format.
> Side note: the interior door texture is contained within the exterior textures themselves, so do be aware of that.

---

## Create a Datapack
1. Follow the tutorial on the [Minecraft Wiki on datapacks](https://minecraft.wiki/w/Data_pack) on how to set one up, use the namespace from earlier

2. Create a new **.json** file in the path

> `data/(namespace)/exterior/(exterior_texture_name).json`

3. Inside this new .json file, paste

> ```json
> {
>   "id": "(namespace):(exterior_texture_name)",
>   "category": "(namespace):exterior/(refer to example below)",
>   "parent": "(namespace):exterior/(refer to example below)",
>   "texture": "(namespace):textures/(exterior_texture_name).png",
>   "emission": "(namespace):textures/(exterior_texture_name)_emission.png"
> }
> ```

4. replacing the **namespace** and the **exterior_texture_name** with your own from earlier

5. Now put this **datapack** into Minecraft.

## Create A Resource Pack
[Follow this guide](https://minecraft.wiki/w/Tutorials/Creating_a_resource_pack)

1. Place your **.png** exterior texture in this path

> `data/(namespace)/exterior/(exterior_texture_name).png`

2. Place your .**png** exterior texture emission in the same path

> `data/(namespace)/exterior/(exterior_texture_name)_emission.png`

3. If you want people to be able to see your exterior texture variant, they will need this **resource pack**.

## Example Reference That's Easy to Follow :)
> This is just an example, do not use the ID "(namespace)" and make sure you're not using any parentheses. Those are just there for making sure you replace (namespace) with your own ID and (exterior_texture_name) with your texture's name.
>> Also see > [Exterior Variant Categories]({{ site.baseurl }}{% post_url 2024-02-05-custom-categories %})

Once you're finished, the json file should look like this:
```json
{
  "id": "(namespace):my_exterior",
  "category": "(namespace):exterior/police_box",
  "parent": "(namespace):exterior/police_box/default",
  "texture": "(namespace):textures/(exterior_texture_name).png",
  "emission": "(namespace):textures/(exterior_texture_name)_emission.png"
}
```
And your resourcepack directory should look like this:
```(namespace)/assets/textures/```

And once you make sure the textures are inside of the directory above, then you're ready to use your new exterior texture variant!

## Specifics About the Different Exterior Categories
> - Police Box = `police_box`
> - Classic Police Box = `classic`
> - K2 Phone Booth = `booth`
> - TT Capsule = `capsule`
> - Moyai = `easter_head`
> - Plinth = `plinth`
> - TARDIM = `tardim`
> - Siege Mode = `siege_mode`
> > FYI: the "doom" and "cube" exteriors are either deprecated or not for use.

## Exterior Subcategories
> - Police Box (I am only listing the ones that have different models):
> > `default`
> > `coral`
> > `tokamak`
> - Classic Police Box:
> > `default`
> - K2 Phone Booth:
> > `default`
> - TT Capsule:
> > `default`
> - Moyai:
> > `default`
> - Plinth:
> >`default`
> - TARDIM:
> > `default`
> - Siege Mode:
> > `default`