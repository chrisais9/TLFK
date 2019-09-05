from __future__ import unicode_literals

from django.shortcuts import render, redirect
from django.http import JsonResponse

import json

class Block:
    def __init__(self,location1,location2,direction_list):
        self.location1=location1 # 왼쪽 위 좌표 => [X,Y]
        self.location2=location2 # 오른쪽 아래 좌표 => [X,Y]
        self.direction_list=direction_list #[[번호, 방향]]
    def in_block(self,current_x,current_y): # 현재 이 블럭에 유저가 있는지 판단
        if(self.location1[0]<= current_x <= self.location2[0] and self.location2[1] <= current_y <=self.location1[1]):
            return True
        else:
            return False
    
    def go_where(self,direction): # 현재 유저의 방향을 따져서 목적지 블록이 어딘지 반환
        for d in self.direction_list:
            if(d[1]==direction):
                return d[0]
        return -1

class CrossWalk:
    def __init__(self,start_id,end_id,time_start,time_end):
        self.start_id=start_id
        self.end_id=end_id
        self.time_start=time_start
        self.time_end=time_end

def login(request):
    return JsonResponse({'status': 'Success'})

def find_direction(di):
    if(0<=di<45 or di>315):
        return 'N'
    elif(135<di<215):
        return 'S'
    elif(45<di<135):
        return 'E'
    elif(215<di<315):
        return 'W'        

def LightInfo(request):
    x = request.GET['x']
    y = request.GET['y']
    di = request.GET['di']
    time = request.GET['time']
    #CrossWalkId = request.GET['CrossWalkId']
    direction = find_direction(int(di))

    BlockList = []
    BlockList.append(Block([-30,30],[-10,10],[[1,'E'],[2,'S']]))
    BlockList.append(Block([ 10,30],[ 30,10],[[0,'W'],[3,'S']]))
    BlockList.append(Block([-30,-10],[-10,30],[[0,'N'],[3,'E']]))
    BlockList.append(Block([10,-10],[30,-30],[[1,'N'],[2,'W']]))

    CrossWalkList=[]
    CrossWalkList.append(CrossWalk(0,1,0,25))
    CrossWalkList.append(CrossWalk(1,3,25,50))
    CrossWalkList.append(CrossWalk(2,3,50,75))
    CrossWalkList.append(CrossWalk(2,0,75,100))

    for i,j in enumerate(BlockList):
        if(j.in_block(int(x),int(y))):
            current_block = i

    
    next_block = BlockList[current_block].go_where(direction)
    
    start_t=-1
    end_t=-1
    for i in CrossWalkList:
        if(i.start_id==current_block and i.end_id == next_block) or (i.start_id==next_block and i.end_id == current_block):
            start_t=i.time_start
            end_t=i.time_end

    msg = ""
    left_time=987654321
    if start_t <= (int(time)%100) <= end_t :
        msg="green"
        left_time=end_t-(int(time)%100)
    else:
        msg="red"
    
    return JsonResponse([{'X': x}, {'Y': y},{'msg':msg},{'start_t':start_t},{'end_t':end_t}], safe=False)
