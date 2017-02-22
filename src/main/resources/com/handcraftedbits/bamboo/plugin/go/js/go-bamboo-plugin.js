/*
 * Copyright (C) 2015-2017 HandcraftedBits
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
(function($, BAMBOO) {
     BAMBOO.GBP = {};

     BAMBOO.GBP.GoTaskConfiguration = (function() {
          var index = 0;
          var selectorButton = "span.aui-iconfont-list-add,span.aui-iconfont-list-remove";
          var selectorErrors = "div.gbp-package-error";
          var selectorIndices = "#_gbp_indices";
          var selectorPackageIndex = "[data-package-index]";
          var selectorPackageName = "input[id^='_gbp_packageName_']";

          var collectIndices = function() {
               var result = [];

               $(selectorPackageIndex).each(function() {
                    result.push(parseInt($(this).attr("data-package-index")));
               });

               return result;
          };

          var filterPackageKeypress = function(event) {
               // Don't allow spaces in package names.

               return (event.keyCode != 32);
          };

          var getNewIndex = function() {
               var index = -1;

               $(selectorPackageIndex).each(function() {
                    var curIndex = parseInt($(this).attr("data-package-index"));

                    if (curIndex > index) {
                         index = curIndex;
                    }
               });

               return index + 1;
          };

          var onPackageAddButtonClicked = function(item) {
               var newItem;

               // Validate all packages in case a blur event was not triggered.

               validatePackages();

               // If there are any validation errors don't allow a new package to be added.

               if ($(selectorErrors).length > 0) {
                    return;
               }

               // Change "add" button to "remove" button for the current package.

               item.removeClass("aui-iconfont-list-add").addClass("aui-iconfont-list-remove");

               // Create a new package and add it to the end of the table.

               newItem = $(AJS.template.load("gbp-widget-packages-template").fill({
                    index: index++
               }).toString()).appendTo($(item).closest("tbody"));

               newItem.find(selectorPackageName).on("blur", validatePackage);
               newItem.find(selectorPackageName).on("keydown", filterPackageKeypress);
               newItem.find("span").parent().on("click", onPackageButtonClicked);
          };

          var onPackageButtonClicked = function() {
               var item = $(this).find(".aui-iconfont-list-add");

               if (item.length > 0) {
                    onPackageAddButtonClicked(item);
               }

               else {
                    onPackageRemoveButtonClicked($(this).closest("tr"));
               }
          };

          var onPackageRemoveButtonClicked = function(item) {
               item.remove();
          };

          var validatePackage = function(pkg) {
               var value = pkg.attr("value");

               // Remove the error div, if available.
               
               pkg.parent().find("div.error").remove();

               if (!value || !value.trim()) {
                    var errorItem = $(AJS.template.load("gbp-widget-package-error-template").toString());
                    
                    pkg.after(errorItem);
               }
          };

          var validatePackages = function() {
               $(selectorPackageName).each(function() {
                    validatePackage($(this));
               })
          };

          return {
               init: function() {
                    $(function() {
                         $(selectorIndices).closest("form").submit(function() {
                              $(selectorIndices).attr("value", collectIndices());

                              // Validate packages in case a blur or click event hasn't been triggered yet.

                              validatePackages();

                              // Prevent form submission if there are validation errors.

                              return ($(selectorErrors).length == 0);
                         });

                         // Determine starting index for added packages.

                         index = getNewIndex();

                         // Add event handlers.

                         $(selectorPackageName).on("blur", validatePackage);
                         $(selectorPackageName).on("keydown", filterPackageKeypress);
                         $(selectorButton).parent().on("click", onPackageButtonClicked);
                    });
               }
          };
     }());
}(jQuery, window.BAMBOO = (window.BAMBOO || {})));