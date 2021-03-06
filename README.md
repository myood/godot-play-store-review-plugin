# godot-play-store-review-plugin
PlayStore Review Plugin for Godot 3.2.2+

# ATTENTION
*This plugin is provided as-is, without any warranty, without any support. If I have time I'll try to reply to any issues rised.

## Functionality

This plugin allows to ask the Android user for a review of your game, using the `Google Play In-App Review API`. For the API itself see [here](https://developer.android.com/guide/playcore/in-app-review).

![](/preview.jpeg "Screenshot")

## Export Setup

1. Setup android export as usual, but with custom build template enabled.
1. Download and move files GodotPlayStoreReview.release.gdap and GodotPlayStoreReview.release.aar to the res://android/plugins. The 'android' directory should be created during the step 1 in your project root directory. If 'plugins' subdir does not exist, simply create it.
1. Set minimum SDK to 21 - Open the res://android/config.gradle, find line 'minSdk: XYZ' and make sure it is equal or greater then 21.
    - alternatively call `sed "s@\(minSdk[[:space:]]\+:[[:space:]]\+\)[[:digit:]]\+@\121@g" -i android/build/config.gradle` as part of your build steps.
3. Start Godot Editor, it should print to the console - 'Found Android Plugin GodotPlayStoreReview'. If not, something went wrong and you should redo previous steps.
4. Open 'Project -> Export...', then under export 'Options' find 'Plugins' and check the checkbox with 'GodotPlayStoreReview'.

## Usage in GDscript & API:

To access the plugin:

```
var review_plugin = null
    
func init_review_plugin():
    if Engine.has_singleton("GodotPlayStoreReview"):
        review_plugin = Engine.get_singleton("GodotPlayStoreReview")
    else:
        print_debug("GodotPlayStoreReview - plugin not available")
```

The plugin has one method 'start_review'. After a call plugin will eventually emit a signal 'finished' with status_code when operation is finished.
The example use is:

```
func start_review():
    if not review_plugin:
        return
    review_plugin.connect("finished", self, "on_review_finished", [], CONNECT_ONESHOT)
    review_plugin.start_review()

func on_review_finished(status_code):
    print_debug("REVIEW FINISHED: status code = " + str(status_code))
```

The `Google Play In-App Review API` itself doesn't say if review was actually done (or even if the review GUI showed up). The API only says if the review flow finished. The plugin on the other hand, reports a few status codes:

```
    STATUS_SUCCESS = 0;                  # this only means that no errors occured, it doesn't guarantee if review was actually done
    REVIEW_MANAGER_CREATION_FAILED = 1;
    REVIEW_TASK_CREATION_FAILED = 2;
```

They will be more clear if you follow the examples in [the API overview](https://developer.android.com/guide/playcore/in-app-review).

*From my experience once you call start_review(), then every subsequent call will "do nothing".*

## License

Distributed under [WTFPL](https://en.wikipedia.org/wiki/WTFPL#Version_2), see LICENSE.md in this repository.
It is very appreciated, if you somehow link to this repo in your work, so that other people could also benefit from this. Later on I'd like to share the source code, so maybe if there are updates needed then people can contribute. Or simply build upon this plugin some more sophisticated stuff.
