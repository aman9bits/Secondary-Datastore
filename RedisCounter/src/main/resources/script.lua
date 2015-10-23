local function notInTable(tbl, item)
    for key, value in pairs(tbl) do
        if value == item then
            return false
        end
    end
    return true
end

local initialList = redis.call('zrangebyscore',KEYS[1],ARGV[1],ARGV[2])

local arr1={}
local arr2={}
local arr3={}
local arr4={}
local i=1
local j=1
local k=1
local l=1


local commonJSON = cjson.decode(ARGV[3])
local singleValueCommonJSON = {}
local multiValueCommonJSON = {}


local differentJSON1 = cjson.decode(ARGV[4])
local singleValueDifferentJSON1 = {}
local multiValueDifferentJSON1 = {}


local differentJSON2 = cjson.decode(ARGV[5])
local singleValueDifferentJSON2 = {}
local multiValueDifferentJSON2 = {}


local differentJSON3 = cjson.decode(ARGV[6])
local singleValueDifferentJSON3 = {}
local multiValueDifferentJSON3 = {}


local differentJSON4 = cjson.decode(ARGV[7])
local singleValueDifferentJSON4 = {}
local multiValueDifferentJSON4 = {}


for key, value in pairs(commonJSON) do
    if type(value) == "table" then
        multiValueCommonJSON[key] = value
    else
        singleValueCommonJSON[key] = value
    end
end


for key, value in pairs(differentJSON1) do
    if type(value) == "table" then
        multiValueDifferentJSON1[key] = value
    else
        singleValueDifferentJSON1[key] = value
    end
end


for key, value in pairs(differentJSON2) do
    if type(value) == "table" then
        multiValueDifferentJSON2[key] = value
    else
        singleValueDifferentJSON2[key] = value
    end
end


for key, value in pairs(differentJSON3) do
    if type(value) == "table" then
        multiValueDifferentJSON3[key] = value
    else
        singleValueDifferentJSON3[key] = value
    end
end


for key, value in pairs(differentJSON4) do
    if type(value) == "table" then
        multiValueDifferentJSON4[key] = value
    else
        singleValueDifferentJSON4[key] = value
    end
end


for temp, v in pairs(initialList) do

    local json = cjson.decode(v)

    local flag = true
    local flag1 = true
    local flag2 = true
    local flag3 = true
    local flag4 = true

    for key, value in pairs(singleValueCommonJSON) do
        if json[key] ~= value then
            flag = false
            break
        end
    end

    if flag == true then
        for key, value in pairs(multiValueCommonJSON) do
            if notInTable(value,json[key]) then
                    flag = false
                    break
            end
        end
    end

    if flag == true then
        for key, value in pairs(singleValueDifferentJSON1) do
            if json[key] ~= value then
                flag1 = false
                break
            end
        end

        if flag1 == true then
            for key, value in pairs(multiValueDifferentJSON1) do
                if notInTable(value,json[key]) then
                    flag1 = false
                    break
                end
            end
        end
        for key, value in pairs(singleValueDifferentJSON2) do
            if json[key] ~= value then
                flag2 = false
                break
            end
        end

        if flag2 == true then
            for key, value in pairs(multiValueDifferentJSON2) do
                if notInTable(value,json[key]) then
                    flag2 = false
                    break
                end
            end
        end

        for key, value in pairs(singleValueDifferentJSON3) do
            if json[key] ~= value then
                flag3 = false
                break
            end
        end

        if flag3 == true then
            for key, value in pairs(multiValueDifferentJSON3) do
                if notInTable(value,json[key]) then
                    flag3 = false
                    break
                end
            end
        end
        for key, value in pairs(singleValueDifferentJSON4) do
            if json[key] ~= value then
                flag4 = false
                break
            end
        end

        if flag4 == true then
            for key, value in pairs(multiValueDifferentJSON4) do
                if notInTable(value,json[key]) then
                    flag4 = false
                    break
                end
            end
        end


        if flag == true and flag1==true then
            i = i+1
            arr1[i]=json[ARGV[8]]
        end
        if flag == true and flag2==true then
            j = j+1
            arr2[j]=json[ARGV[8]]
        end
        if flag == true and flag3==true then
            k = k+1
            arr3[k]=json[ARGV[8]]
        end
        if flag == true and flag4==true then
            l = l+1
            arr4[l]=json[ARGV[8]]
        end
    end
end

arr1[1]=i-1
arr2[1]=j-1
arr3[1]=k-1
arr4[1]=l-1

local arr_final={}
arr_final[1]=arr1
arr_final[2]=arr2
arr_final[3]=arr3
arr_final[4]=arr4
return arr_final


