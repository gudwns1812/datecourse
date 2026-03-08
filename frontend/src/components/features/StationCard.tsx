"use client";

import Badge from "@/components/common/Badge";

interface StationCardProps {
  title: string;
  description: string;
  imageUrl: string;
  priceLevel: string;
  category: string;
  tags: string[];
  icon: string;
}

export default function StationCard({
  title,
  description,
  imageUrl,
  priceLevel,
  category,
  tags,
  icon,
}: StationCardProps) {
  return (
    <div className="group bg-white dark:bg-slate-800/50 rounded-xl overflow-hidden border border-primary/5 shadow-sm hover:shadow-md transition-shadow">
      <div className="md:flex">
        <div className="md:w-1/3 h-48 md:h-auto overflow-hidden">
          <div
            className="w-full h-full bg-cover bg-center transition-transform duration-500 group-hover:scale-105"
            style={{ backgroundImage: `url('${imageUrl}')` }}
          />
        </div>
        <div className="p-6 md:w-2/3">
          <div className="flex justify-between items-start mb-2">
            <div className="flex items-center gap-2">
              <span className="material-symbols-outlined text-primary text-xl">{icon}</span>
              <h3 className="text-xl font-bold text-slate-900 dark:text-slate-100">{title}</h3>
            </div>
            <span className="text-sm font-semibold text-primary">{priceLevel}</span>
          </div>
          <p className="text-slate-600 dark:text-slate-400 mb-4 leading-relaxed">
            {description}
          </p>
          <div className="flex flex-wrap gap-2">
            <Badge variant="primary">{category}</Badge>
            {tags.map((tag) => (
              <Badge key={tag} variant="secondary">{tag}</Badge>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
