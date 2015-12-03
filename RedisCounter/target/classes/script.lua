--[[ Returns true if an item is not present in the table, else returns false --]]
local function notInTable(tbl, item)
    for key, value in pairs(tbl) do
        if value == item then
            return false
        end
    end
    return true
end

--[[ Fetch redis list between the time range specified by ARGV[1] and ARGV[2] --]]
local initialList = redis.call('zrangebyscore',KEYS[1],ARGV[1],ARGV[2])

local arr={}
local counter={}
local singleValueDifferentJSON={}
local multiValueDifferentJSON={}

local commonJSON = cjson.decode(ARGV[3])
local singleValueCommonJSON = {}
local multiValueCommonJSON = {}

local differentJSON = cjson.decode(ARGV[4])


for i=1,ARGV[6],1 do
    arr[i]={}
    counter[i]=1
    singleValueDifferentJSON[i]={}
    multiValueDifferentJSON[i]={}
    for key,value in pairs(differentJSON[i]) do
        if type(value) == "table" then
            multiValueDifferentJSON[i][key] = value
        else
            singleValueDifferentJSON[i][key] = value
        end
    end
end

for key, value in pairs(commonJSON) do
    if type(value) == "table" then
        multiValueCommonJSON[key] = value
    else
        singleValueCommonJSON[key] = value
    end
end


for temp, v in pairs(initialList) do

    local json = cjson.decode(v)

    local commonFlag = true
    local diffFlag={}
    for i=1,ARGV[6],1 do
        diffFlag[i] = true
    end
    for key, value in pairs(singleValueCommonJSON) do
        if json[key] ~= value then
            commonFlag = false
            break
        end
    end

    if commonFlag == true then
        for key, value in pairs(multiValueCommonJSON) do
            if notInTable(value,json[key]) then
                commonFlag = false
                break
            end
        end
    end


    if commonFlag == true then
        for i=1,ARGV[6],1 do
            for key, value in pairs(singleValueDifferentJSON[i]) do
                if json[key] ~= value then
                    diffFlag[i] = false
                    break
                end
            end
            if diffFlag[i] == true then
                for key, value in pairs(multiValueDifferentJSON[i]) do
                    if notInTable(value,json[key]) then
                        diffFlag[i] = false
                        break
                    end
                end
            end
        end
    end
    for i=1,ARGV[6],1 do
        if commonFlag == true and diffFlag[i] == true then
            counter[i] = counter[i]+1
            arr[i][counter[i]]=json[ARGV[5]]
        end
    end
end
for i=1,ARGV[6],1 do
    arr[i][1]=counter[i]-1
end
local arr_final={}
for i=1,ARGV[6],1 do
    arr_final[i]=arr[i]
end
return arr_final