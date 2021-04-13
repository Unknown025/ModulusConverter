# EXWPack.info

## This file defines basic metadata for your content pack.

| Keyword | Type | Default | Required | Explanation |
|---|---|---|---|---|
| packId | String | Content pack filename | :heavy_check_mark: | This pack's **unique** identifier. |
| authorList | String[] | [] |  | List of authors for this pack. |
| url | String | - |  | URL for this pack's website. |
| version | String | 1.0.0 |  | Pack version. |
| logo | String | "" |  | Logo file. |
| name | String | "" |  | Name of this pack. |
| credits | String | "" |  | Credits for this pack. |

This file should be in the root directory of your content pack.
If it cannot be located at runtime, default values will be used.

It will still be loaded from Flan's or Modulus 
compatible content packs.