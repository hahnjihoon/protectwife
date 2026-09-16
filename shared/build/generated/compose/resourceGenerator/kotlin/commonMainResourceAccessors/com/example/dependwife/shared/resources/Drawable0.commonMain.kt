@file:OptIn(InternalResourceApi::class)

package com.example.dependwife.shared.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.ResourceContentHash
import org.jetbrains.compose.resources.ResourceItem

private const val MD: String = "composeResources/com.example.dependwife.shared.resources/"

@delegate:ResourceContentHash(1_119_806_274)
internal val Res.drawable.enemy_common: DrawableResource by lazy {
      DrawableResource("drawable:enemy_common", setOf(
        ResourceItem(setOf(), "${MD}drawable/enemy_common.png", -1, -1),
      ))
    }

@delegate:ResourceContentHash(1_079_494_458)
internal val Res.drawable.enemy_midboss: DrawableResource by lazy {
      DrawableResource("drawable:enemy_midboss", setOf(
        ResourceItem(setOf(), "${MD}drawable/enemy_midboss.png", -1, -1),
      ))
    }

@delegate:ResourceContentHash(404_985_317)
internal val Res.drawable.husband_character: DrawableResource by lazy {
      DrawableResource("drawable:husband_character", setOf(
        ResourceItem(setOf(), "${MD}drawable/husband_character.png", -1, -1),
      ))
    }

@delegate:ResourceContentHash(1_938_483_215)
internal val Res.drawable.weapon_board_1: DrawableResource by lazy {
      DrawableResource("drawable:weapon_board_1", setOf(
        ResourceItem(setOf(), "${MD}drawable/weapon_board_1.png", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-23_720_527)
internal val Res.drawable.weapon_board_2: DrawableResource by lazy {
      DrawableResource("drawable:weapon_board_2", setOf(
        ResourceItem(setOf(), "${MD}drawable/weapon_board_2.png", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-93_739_322)
internal val Res.drawable.weapon_board_3: DrawableResource by lazy {
      DrawableResource("drawable:weapon_board_3", setOf(
        ResourceItem(setOf(), "${MD}drawable/weapon_board_3.png", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-1_849_401_160)
internal val Res.drawable.weapon_knife_1: DrawableResource by lazy {
      DrawableResource("drawable:weapon_knife_1", setOf(
        ResourceItem(setOf(), "${MD}drawable/weapon_knife_1.png", -1, -1),
      ))
    }

@delegate:ResourceContentHash(352_475_841)
internal val Res.drawable.weapon_knife_2: DrawableResource by lazy {
      DrawableResource("drawable:weapon_knife_2", setOf(
        ResourceItem(setOf(), "${MD}drawable/weapon_knife_2.png", -1, -1),
      ))
    }

@delegate:ResourceContentHash(1_715_352_766)
internal val Res.drawable.weapon_knife_3: DrawableResource by lazy {
      DrawableResource("drawable:weapon_knife_3", setOf(
        ResourceItem(setOf(), "${MD}drawable/weapon_knife_3.png", -1, -1),
      ))
    }

@delegate:ResourceContentHash(2_147_165_214)
internal val Res.drawable.weapon_tv_1: DrawableResource by lazy {
      DrawableResource("drawable:weapon_tv_1", setOf(
        ResourceItem(setOf(), "${MD}drawable/weapon_tv_1.png", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-1_956_133_996)
internal val Res.drawable.weapon_tv_2: DrawableResource by lazy {
      DrawableResource("drawable:weapon_tv_2", setOf(
        ResourceItem(setOf(), "${MD}drawable/weapon_tv_2.png", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-318_096_039)
internal val Res.drawable.weapon_tv_3: DrawableResource by lazy {
      DrawableResource("drawable:weapon_tv_3", setOf(
        ResourceItem(setOf(), "${MD}drawable/weapon_tv_3.png", -1, -1),
      ))
    }

@delegate:ResourceContentHash(168_594_261)
internal val Res.drawable.weapon_washer_1: DrawableResource by lazy {
      DrawableResource("drawable:weapon_washer_1", setOf(
        ResourceItem(setOf(), "${MD}drawable/weapon_washer_1.png", -1, -1),
      ))
    }

@delegate:ResourceContentHash(1_639_568_166)
internal val Res.drawable.weapon_washer_2: DrawableResource by lazy {
      DrawableResource("drawable:weapon_washer_2", setOf(
        ResourceItem(setOf(), "${MD}drawable/weapon_washer_2.png", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-1_093_204_788)
internal val Res.drawable.weapon_washer_3: DrawableResource by lazy {
      DrawableResource("drawable:weapon_washer_3", setOf(
        ResourceItem(setOf(), "${MD}drawable/weapon_washer_3.png", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-1_322_685_303)
internal val Res.drawable.wife_character: DrawableResource by lazy {
      DrawableResource("drawable:wife_character", setOf(
        ResourceItem(setOf(), "${MD}drawable/wife_character.png", -1, -1),
      ))
    }

@InternalResourceApi
internal fun _collectCommonMainDrawable0Resources(map: MutableMap<String, DrawableResource>) {
  map.put("enemy_common", Res.drawable.enemy_common)
  map.put("enemy_midboss", Res.drawable.enemy_midboss)
  map.put("husband_character", Res.drawable.husband_character)
  map.put("weapon_board_1", Res.drawable.weapon_board_1)
  map.put("weapon_board_2", Res.drawable.weapon_board_2)
  map.put("weapon_board_3", Res.drawable.weapon_board_3)
  map.put("weapon_knife_1", Res.drawable.weapon_knife_1)
  map.put("weapon_knife_2", Res.drawable.weapon_knife_2)
  map.put("weapon_knife_3", Res.drawable.weapon_knife_3)
  map.put("weapon_tv_1", Res.drawable.weapon_tv_1)
  map.put("weapon_tv_2", Res.drawable.weapon_tv_2)
  map.put("weapon_tv_3", Res.drawable.weapon_tv_3)
  map.put("weapon_washer_1", Res.drawable.weapon_washer_1)
  map.put("weapon_washer_2", Res.drawable.weapon_washer_2)
  map.put("weapon_washer_3", Res.drawable.weapon_washer_3)
  map.put("wife_character", Res.drawable.wife_character)
}
