var $MessageConst = {
	"$Loading.0" : "\u6578\u64DA\u88DD\u8F09\u4E2D...",
	SaveOK : "\u64CD\u4F5C\u6210\u529F",
	"Button.Cancel" : "\u53D6\u6D88\uFF0C",
	"Button.More" : "\u66F4\u591A",
	"Button.Less" : "\u66F4\u5C11",
	"Button.Location.Reload" : "\u91CD\u65B0\u88DD\u8F09\u7576\u524D\u9801\u9762",
	"Error.Title" : "\u7CFB\u7D71\u7570\u5E38",
	"Error.delete2" : "\u81F3\u5C11\u8981\u9078\u64C7\u4E00\u500B\u689D\u76EE",
	"Button.Search" : "\u641C\u7D22",
	"Text.Search" : "\u8ACB\u8F38\u5165\u95DC\u9375\u5B57",
	"Tree.dropConfirm" : "\u662F\u5426\u78BA\u8A8D\u62D6\u653E\u64CD\u4F5C",
	"ImageSlide.0" : "\u6253\u958B\u5716\u7247",
	"ImageSlide.1" : "\u555F\u52D5\u64AD\u653E",
	"ImageSlide.2" : "\u505C\u6B62\u64AD\u653E",
	"ValidateCode.0" : "\u770B\u4E0D\u6E05\u695A\uFF0C\u518D\u63DB\u4E00\u5F35",
	"Table.Draggable.0" : "\u6B63\u5728\u62D6\u52D5",
	"Table.Draggable.1" : "\u500B\u9805\u76EE",
	"Progressbar.0" : "\u4E2D\u6B62",
	"Progressbar.1" : "\u8A73\u7D30\u4FE1\u606F",
	"Progressbar.2" : "\u6E05\u9664\u6240\u6709",
	"Progressbar.3" : "\u8207\u670D\u52D9\u5668\u540C\u6B65\u4FE1\u606F",
	"Progressbar.4" : "\u7981\u6B62\u6EFE\u52D5",
	"Progressbar.5" : "\u95DC\u9589",
	"PwdStrength.0" : [ "\u975E\u5E38\u5F31", "\u5F31", "\u7A0D\u5F31",
			"\u4E00\u822C", "\u7A0D\u5F3A", "\u5F3A", "\u975E\u5E38\u5F3A",
			"\u5B8C\u7F8E" ],
	"PwdStrength.1" : "\u5BC6\u78BC\u5F37\u5EA6",
	"Calendar.0" : "\u65E5:\u4E00:\u4E8C:\u4E09:\u56DB:\u4E94:\u516D",
	"Calendar.1" : "\u4ECA\u5929",
	"Calendar.2" : "\u6E05\u9664"
};

Validation_TT = {
	required : "\u5B58\u5728\u5FC5\u586B\u9805",
	number : "\u8ACB\u8F38\u5165\u6709\u6548\u7684\u6578\u5B57",
	digits : "\u8ACB\u8F38\u5165\u4E00\u500B\u6578\u5B57\u3002\u4E0D\u5141\u8A31\u8F38\u5165\u7A7A\u683C\uFF0C\u9017\u865F\uFF0C\u5206\u865F\u7B49\u5B57\u7B26",
	alpha : "\u8ACB\u8F38\u5165\u82F1\u6587\u5B57\u6BCD",
	alphanum : "\u8ACB\u8F38\u5165\u82F1\u6587\u5B57\u6BCD\u6216\u662F\u6578\u5B57\uFF0C\u5176\u5B83\u5B57\u7B26\u662F\u4E0D\u5141\u8A31\u7684",
	date : "\u8ACB\u4F7F\u7528\u9019\u6A23\u7684\u65E5\u671F\u683C\u5F0F\uFF1A\uFF05s\u7684\u3002\u4F8B\u5982\uFF1A\uFF05S",
	email : "\u8ACB\u8F38\u5165\u6709\u6548\u7684\u90F5\u4EF6\u5730\u5740,:\u5982:username@example.com",
	url : "\u8ACB\u8F38\u5165\u6709\u6548\u7684URL\u5730\u5740",
	min_value : "\u6240\u5141\u8A31\u7684\u6700\u5C0F\u503C\u70BA:%s",
	max_value : "\u6240\u5141\u8A31\u7684\u6700\u5927\u503C\u70BA:%s",
	min_length : "\u6700\u5C0F\u9577\u5EA6\u70BA\uFF05s\u7684\uFF0C\u7576\u524D\u9577\u5EA6\u70BA\uFF05s",
	max_length : "\u6700\u5927\u9577\u5EA6\u70BA\uFF05s\u7684\uFF0C\u7576\u524D\u9577\u5EA6\u70BA%s",
	int_range : "\u8F38\u5165\u503C\u61C9\u8A72\u70BA\u8A66\u7528\uFF05s:-:\uFF05s\u7684\u7684\u6574\u6578",
	float_range : "\u8F38\u5165\u503C\u61C9\u8A72\u70BA\u8A66\u7528\uFF05s:-:\uFF05s\u7684\u7684\u6578\u5B57",
	length_range : "\u8F38\u5165\u503C\u7684\u9577\u5EA6\u61C9\u8A72\u5728\u8A66\u7528\uFF05s:-:\uFF05s\u7684\u4E4B\u9593\uFF0C\u7576\u524D\u9577\u5EA6\u70BA\uFF05s\u7684",
	file : "\u6587\u4EF6\u985E\u578B\u61C9\u8A72\u70BA[\uFF05\u4E00]\u5176\u4E2D\u4E4B\u4E00",
	chinese : "\u8ACB\u8F38\u5165\u4E2D\u6587",
	ip : "\u8ACB\u8F38\u5165\u6B63\u78BA\u7684IP\u5730\u5740",
	phone : "\u8ACB\u8F38\u5165\u6B63\u78BA\u7684\u96FB\u8A71\u865F\u78BC\uFF0C\u5982\uFF1A010-88886666\uFF0C\u7576\u524D\u9577\u5EA6\u70BA\uFF05s\u7684",
	mobile_phone : "\u8ACB\u8F38\u5165\u6B63\u78BA\u7684\u624B\u6A5F\u865F\u78BC\uFF0C\u7576\u524D\u9577\u5EA6\u70BA\uFF05s\u7684",
	equals : "\u8207\u4E0A\u9762\u4E0D\u4E00\u81F3\uFF0C\u8ACB\u91CD\u65B0\u8F38\u5165",
	less_than : "\u61C9\u8A72\u5C0F\u65BC\u524D\u9762\u7684\u503C",
	great_than : "\u61C9\u8A72\u5927\u65BC\u524D\u9762\u7684\u503C"
};