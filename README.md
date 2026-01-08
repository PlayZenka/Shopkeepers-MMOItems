# Shopkeepers + MMOItems

This plugin is designed for stable integration of **Shopkeepers** with items from **MMOItems**.

Its purpose is to eliminate the incompatibility between Shopkeepers and MMOItems, which can cause trades involving MMOItems to fail or be blocked.

The plugin **does not add new mechanics** but ensures correct functionality of existing trades.

---

## Main Problem

Shopkeepers strictly compares items during trades. Over time, the same MMOItem can **differ from the saved trade**, causing Shopkeepers to block the transaction.

---

## How This Plugin Solves the Problem

The plugin intercepts the **UpdateItemEvent** from Shopkeepers and:

1. Determines if the item is an MMOItem
2. Retrieves the canonical ItemStack from MMOItems using TYPE + ID
3. Normalizes the trade item to the canonical version
4. Replaces the item **before Shopkeepers performs its check**

This ensures:

* all trades use the same ItemStack
* Shopkeepers considers the items identical
* trades proceed reliably

---

## How MMOItems Are Determined

An item is considered an MMOItem if:

* it has a type (`NBTItem.hasType()`)
* it contains the tag `MMOITEMS_ITEM_ID`

If either condition is not met, the item is ignored and not modified.

Vanilla items and items from other plugins **are not affected**.

---

## What the Check and Update Actually Do

During the UpdateItemEvent:

* The trade ItemStack is retrieved
* MMOItems TYPE and ID are extracted
* The canonical ItemStack is retrieved from MMOItems
* A comparison is performed
* If they do not match, the item is updated

---

## Limitations (Important to Understand)

* Dynamic MMOItems data (e.g., damage variance) **does not work**

---

## Configuration

```yaml
enable-item-updater: true
debug: false
```

### debug

* `false` — logging is disabled
* `true` — each MMOItem update in trades is logged

---

## Dependencies

### Required

* **Shopkeepers** (API is used directly)
* **MMOItems**
* **MythicLib** (dependency of MMOItems)

### Optional

* None

---

## Compatibility and Testing

The plugin was tested on:

* **Paper 1.21.4**
* Shopkeepers-2.25.0
* MMOItems-6.10.1-32
* MythicLib-dist-1.7.1-33

---

## Contact

* Discord - @PlayZenka
