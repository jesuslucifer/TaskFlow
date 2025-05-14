import { Component } from '@angular/core';
import { ProjectCardComponent } from '../project-card/project-card.component';
import { IProject } from '../../../core/interface/project.interface';

@Component({
  selector: 'app-project-list',
  imports: [ProjectCardComponent],
  templateUrl: './project-list.component.html',
  styleUrl: './project-list.component.scss',
})
export class ProjectListComponent {
  projects: IProject[] = [
    {
      id: 1,
      name: 'Project 1',
      description: 'This is project 1',
      image: 'https://picsum.photos/200/300',
      owner: 'Иван Иванов',
      createdAt: '12-06-2025',
      tasksCount: '12',
      tasksCompleted: '4',
      tags: [
        {
          tag: 'IOS APP',
        },
        {
          tag: 'UI/UX',
        },
      ],
    },
    {
      id: 2,
      name: 'Project 2',
      description: 'This is project 2',
      image: 'https://picsum.photos/200/301',
      owner: 'Иван Иванов',
      tasksCount: '5',
      tasksCompleted: '4',

      createdAt: '15-03-2025',
      tags: [
        {
          tag: '3',
        },
        {
          tag: '4',
        },
      ],
    },
  ];
}
